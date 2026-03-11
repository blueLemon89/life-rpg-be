package com.liferpg.service.quest.impl;

import com.liferpg.dto.response.QuestCompleteDTO;
import com.liferpg.entity.Character;
import com.liferpg.entity.CharacterQuest;
import com.liferpg.entity.CharacterQuestStatus;
import com.liferpg.entity.CharacterStats;
import com.liferpg.entity.Inventory;
import com.liferpg.entity.Item;
import com.liferpg.entity.LevelConfigEntity;
import com.liferpg.entity.Quest;
import com.liferpg.entity.QuestCompletion;
import com.liferpg.exception.BadRequestException;
import com.liferpg.repository.CharacterQuestRepository;
import com.liferpg.repository.CharacterRepository;
import com.liferpg.repository.CharacterStatsRepository;
import com.liferpg.repository.InventoryRepository;
import com.liferpg.repository.ItemRepository;
import com.liferpg.repository.LevelConfigRepository;
import com.liferpg.repository.QuestCompletionRepository;
import com.liferpg.repository.QuestRepository;
import com.liferpg.service.quest.IQuestService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestServiceImpl implements IQuestService {

  private static final int STARTER_UNLOCK_LEVEL = 1;
  private static final int DEFAULT_XP_PER_LEVEL = 100;

  private final QuestRepository questRepository;
  private final CharacterQuestRepository characterQuestRepository;
  private final QuestCompletionRepository questCompletionRepository;
  private final CharacterRepository characterRepository;
  private final CharacterStatsRepository characterStatsRepository;
  private final InventoryRepository inventoryRepository;
  private final ItemRepository itemRepository;
  private final LevelConfigRepository levelConfigRepository;

  @Override
  @Transactional
  public void generateStarterQuests(UUID characterId) {
    validateCharacterExists(characterId);

    log.info("[Quest] Generating starter quests for character {}", characterId);
    List<Quest> starterQuests = questRepository.findStarterEasyQuests(STARTER_UNLOCK_LEVEL);
    if (starterQuests.isEmpty()) {
      throw new BadRequestException("Starter quests are not configured");
    }
    if (starterQuests.size() < 3) {
      throw new BadRequestException("At least 3 EASY starter quests are required");
    }

    Instant now = Instant.now();
    List<CharacterQuest> assignments = starterQuests.stream()
        .map(quest -> CharacterQuest.builder()
            .id(UUID.randomUUID())
            .characterId(characterId)
            .questId(quest.getId())
            .status(CharacterQuestStatus.ACTIVE)
            .assignedAt(now)
            .build())
        .toList();

    characterQuestRepository.saveAll(assignments);
  }

  @Override
  @Transactional
  public QuestCompleteDTO completeQuest(UUID characterQuestId, UUID userId) {
    Character character = characterRepository.findByUserId(userId)
        .orElseThrow(() -> new BadRequestException("Character not found"));

    CharacterQuest characterQuest = characterQuestRepository.findByIdAndCharacterId(
            characterQuestId,
            character.getId()
        )
        .orElseThrow(() -> new BadRequestException("Character quest not found"));

    if (characterQuest.getStatus() != CharacterQuestStatus.ACTIVE) {
      throw new BadRequestException("Quest is not active");
    }

    Quest quest = questRepository.findById(characterQuest.getQuestId())
        .orElseThrow(() -> new BadRequestException("Quest template not found"));

    int oldTotalXp = Math.max(0, safeInt(character.getXp()));
    int oldGold = Math.max(0, safeInt(character.getGold()));
    List<LevelConfigEntity> levelConfigs = levelConfigRepository.findAllByOrderByLevelAsc();
    LevelProgress oldProgress = calculateLevelProgress(oldTotalXp, levelConfigs);

    int xpEarned = safeInt(quest.getXpReward());
    int goldEarned = safeInt(quest.getGoldReward());
    int newTotalXp = oldTotalXp + xpEarned;
    LevelProgress newProgress = calculateLevelProgress(newTotalXp, levelConfigs);
    Instant completedAt = Instant.now();

    characterQuest.setStatus(CharacterQuestStatus.COMPLETED);
    characterQuest.setCompletedAt(completedAt);
    characterQuestRepository.save(characterQuest);

    QuestCompletion completion = QuestCompletion.builder()
        .id(UUID.randomUUID())
        .characterId(character.getId())
        .questId(quest.getId())
        .xpEarned(xpEarned)
        .goldEarned(goldEarned)
        .completedAt(completedAt)
        .build();
    questCompletionRepository.save(completion);

    character.setXp(newTotalXp);
    character.setGold(oldGold + goldEarned);
    character.setLevel(newProgress.level());
    characterRepository.save(character);

    updateCharacterStats(character.getId(), xpEarned);
    RewardGrant rewardGrant = grantQuestReward(character.getId(), quest);

    return QuestCompleteDTO.builder()
        .questCompleted(Boolean.TRUE)
        .xpGained(xpEarned)
        .goldGained(goldEarned)
        .levelUp(newProgress.level() > oldProgress.level())
        .oldLevel(oldProgress.level())
        .newLevel(newProgress.level())
        .currentExp(newProgress.currentExp())
        .expToNextLevel(newProgress.expToNextLevel())
        .rewardItemId(rewardGrant.itemId())
        .rewardItemName(rewardGrant.itemName())
        .rewardQuantity(rewardGrant.quantity())
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public List<CharacterQuest> getActiveQuests(UUID characterId) {
    return characterQuestRepository.findByCharacterIdAndStatusOrderByAssignedAtAsc(
        characterId,
        CharacterQuestStatus.ACTIVE
    );
  }

  private void validateCharacterExists(UUID characterId) {
    if (!characterRepository.existsById(characterId)) {
      throw new BadRequestException("Character not found");
    }
  }

  private int safeInt(Integer value) {
    return value == null ? 0 : value;
  }

  private int safePositive(Integer value, int fallback) {
    if (value == null || value <= 0) {
      return fallback;
    }
    return value;
  }

  private void updateCharacterStats(UUID characterId, int xpEarned) {
    CharacterStats stats = characterStatsRepository.findById(characterId)
        .orElseGet(() -> CharacterStats.builder()
            .characterId(characterId)
            .currentStreak(0)
            .bestStreak(0)
            .completedQuests(0)
            .totalXp(0)
            .build());

    stats.setCompletedQuests(safeInt(stats.getCompletedQuests()) + 1);
    stats.setTotalXp(safeInt(stats.getTotalXp()) + xpEarned);
    characterStatsRepository.save(stats);
  }

  private RewardGrant grantQuestReward(UUID characterId, Quest quest) {
    UUID rewardItemId = quest.getRewardItemId();
    if (rewardItemId == null) {
      return RewardGrant.none();
    }

    Item item = itemRepository.findById(rewardItemId)
        .orElseThrow(() -> new BadRequestException("Reward item not found"));
    int rewardQuantity = safePositive(quest.getRewardItemQuantity(), 1);

    Inventory inventory = inventoryRepository.findByCharacterIdAndItemId(characterId, rewardItemId)
        .map(existing -> {
          existing.setQuantity(safeInt(existing.getQuantity()) + rewardQuantity);
          return existing;
        })
        .orElseGet(() -> Inventory.builder()
            .id(UUID.randomUUID())
            .characterId(characterId)
            .itemId(rewardItemId)
            .quantity(rewardQuantity)
            .build());

    inventoryRepository.save(inventory);
    return new RewardGrant(item.getId(), item.getName(), rewardQuantity);
  }

  private LevelProgress calculateLevelProgress(int totalXp, List<LevelConfigEntity> levelConfigs) {
    int remainingXp = Math.max(0, totalXp);
    if (levelConfigs == null || levelConfigs.isEmpty()) {
      int level = Math.max(1, (remainingXp / DEFAULT_XP_PER_LEVEL) + 1);
      int currentExp = remainingXp % DEFAULT_XP_PER_LEVEL;
      int expToNextLevel = DEFAULT_XP_PER_LEVEL - currentExp;
      return new LevelProgress(level, currentExp, expToNextLevel);
    }

    int level = 1;
    for (LevelConfigEntity levelConfig : levelConfigs) {
      int xpRequired = safePositive(levelConfig.getXpRequired(), 0);
      if (xpRequired == 0) {
        continue;
      }

      if (remainingXp >= xpRequired) {
        remainingXp -= xpRequired;
        level++;
        continue;
      }

      return new LevelProgress(level, remainingXp, xpRequired - remainingXp);
    }

    return new LevelProgress(level, remainingXp, 0);
  }

  private record LevelProgress(int level, int currentExp, int expToNextLevel) {
  }

  private record RewardGrant(UUID itemId, String itemName, Integer quantity) {

    private static RewardGrant none() {
      return new RewardGrant(null, null, null);
    }
  }
}
