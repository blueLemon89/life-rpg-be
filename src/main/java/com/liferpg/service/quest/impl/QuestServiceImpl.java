package com.liferpg.service.quest.impl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.liferpg.entity.Character;
import com.liferpg.entity.CharacterQuest;
import com.liferpg.entity.CharacterQuestStatus;
import com.liferpg.entity.CharacterStats;
import com.liferpg.entity.Quest;
import com.liferpg.entity.QuestCompletion;
import com.liferpg.exception.BadRequestException;
import com.liferpg.repository.CharacterQuestRepository;
import com.liferpg.repository.CharacterRepository;
import com.liferpg.repository.CharacterStatsRepository;
import com.liferpg.repository.QuestRepository;
import com.liferpg.repository.QuestCompletionRepository;
import com.liferpg.service.quest.IQuestService;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestServiceImpl implements IQuestService {

  private static final int STARTER_UNLOCK_LEVEL = 1;
  private static final int XP_PER_LEVEL = 100;

  private final QuestRepository questRepository;
  private final CharacterQuestRepository characterQuestRepository;
  private final QuestCompletionRepository questCompletionRepository;
  private final CharacterRepository characterRepository;
  private final CharacterStatsRepository characterStatsRepository;

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
  public void completeQuest(UUID characterQuestId) {
    CharacterQuest characterQuest = characterQuestRepository.findById(characterQuestId)
        .orElseThrow(() -> new BadRequestException("Character quest not found"));

    if (characterQuest.getStatus() != CharacterQuestStatus.ACTIVE) {
      throw new BadRequestException("Quest is not active");
    }

    Quest quest = questRepository.findById(characterQuest.getQuestId())
        .orElseThrow(() -> new BadRequestException("Quest template not found"));

    Character character = characterRepository.findById(characterQuest.getCharacterId())
        .orElseThrow(() -> new BadRequestException("Character not found"));

    int xpEarned = safeInt(quest.getXpReward());
    int goldEarned = safeInt(quest.getGoldReward());
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

    int totalXp = safeInt(character.getXp()) + xpEarned;
    character.setXp(totalXp);
    character.setGold(safeInt(character.getGold()) + goldEarned);
    character.setLevel(calculateLevel(totalXp));
    characterRepository.save(character);

    CharacterStats stats = characterStatsRepository.findById(character.getId())
        .orElseGet(() -> CharacterStats.builder()
            .characterId(character.getId())
            .currentStreak(0)
            .bestStreak(0)
            .completedQuests(0)
            .totalXp(0)
            .build());
    stats.setCompletedQuests(safeInt(stats.getCompletedQuests()) + 1);
    stats.setTotalXp(safeInt(stats.getTotalXp()) + xpEarned);
    characterStatsRepository.save(stats);
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

  private int calculateLevel(int totalXp) {
    return Math.max(1, (totalXp / XP_PER_LEVEL) + 1);
  }
}
