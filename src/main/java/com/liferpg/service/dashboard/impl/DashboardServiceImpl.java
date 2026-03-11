package com.liferpg.service.dashboard.impl;

import com.liferpg.dto.response.dashboard.CharacterInfo;
import com.liferpg.dto.response.dashboard.DashboardResponse;
import com.liferpg.dto.response.dashboard.ItemDto;
import com.liferpg.dto.response.dashboard.QuestDto;
import com.liferpg.dto.response.dashboard.RewardDto;
import com.liferpg.dto.response.dashboard.SkillDto;
import com.liferpg.dto.response.dashboard.StatisticsDto;
import com.liferpg.dto.response.dashboard.StatusInfo;
import com.liferpg.entity.Character;
import com.liferpg.entity.CharacterQuestStatus;
import com.liferpg.entity.CharacterStats;
import com.liferpg.entity.QuestDifficulty;
import com.liferpg.entity.QuestType;
import com.liferpg.exception.BadRequestException;
import com.liferpg.repository.CharacterQuestRepository;
import com.liferpg.repository.CharacterRepository;
import com.liferpg.repository.CharacterSkillRepository;
import com.liferpg.repository.CharacterStatsRepository;
import com.liferpg.repository.InventoryRepository;
import com.liferpg.repository.QuestCompletionRepository;
import com.liferpg.repository.RewardRepository;
import com.liferpg.service.dashboard.DashboardService;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

  private final CharacterRepository characterRepository;
  private final CharacterStatsRepository characterStatsRepository;
  private final CharacterSkillRepository characterSkillRepository;
  private final CharacterQuestRepository characterQuestRepository;
  private final InventoryRepository inventoryRepository;
  private final RewardRepository rewardRepository;
  private final QuestCompletionRepository questCompletionRepository;

  @Override
  @Transactional(readOnly = true)
  public DashboardResponse getDashboard(UUID characterId) {
    Character character = characterRepository.findById(characterId)
        .orElseThrow(() -> new BadRequestException("Character not found"));

    CharacterStats stats = characterStatsRepository.findById(characterId)
        .orElseGet(() -> CharacterStats.builder()
            .characterId(characterId)
            .currentStreak(0)
            .bestStreak(0)
            .completedQuests(0)
            .totalXp(0)
            .build());

    List<SkillDto> skills = characterSkillRepository.findDashboardSkillsByCharacterId(characterId)
        .stream()
        .map(projection -> SkillDto.builder()
            .skillCode(projection.getSkillCode())
            .skillName(projection.getSkillName())
            .level(projection.getLevel())
            .xp(projection.getXp())
            .build())
        .toList();

    List<QuestDto> quests = characterQuestRepository.findDashboardQuestsByCharacterIdAndStatus(
            characterId,
            CharacterQuestStatus.ACTIVE.name()
        )
        .stream()
        .map(projection -> QuestDto.builder()
            .characterQuestId(projection.getCharacterQuestId())
            .questId(projection.getQuestId())
            .title(projection.getTitle())
            .difficulty(parseDifficulty(projection.getDifficulty()))
            .xpReward(projection.getXpReward())
            .type(parseType(projection.getType()))
            .build())
        .toList();

    List<ItemDto> inventory = inventoryRepository.findDashboardInventoryByCharacterId(characterId)
        .stream()
        .map(projection -> ItemDto.builder()
            .itemId(projection.getItemId())
            .name(projection.getName())
            .quantity(projection.getQuantity())
            .build())
        .toList();

    List<RewardDto> rewards = rewardRepository.findByCharacterIdOrderByCreatedAtDesc(characterId)
        .stream()
        .map(reward -> RewardDto.builder()
            .rewardId(reward.getId())
            .name(reward.getName())
            .description(reward.getDescription())
            .unlockCondition(reward.getUnlockCondition())
            .isUnlocked(reward.getUnlocked())
            .build())
        .toList();

    StatisticsDto statistics = calculateStatistics(characterId);

    return DashboardResponse.builder()
        .character(CharacterInfo.builder()
            .name(character.getName())
            .level(character.getLevel())
            .xp(character.getXp())
            .gold(character.getGold())
            .hp(character.getHp())
            .maxHp(character.getMaxHp())
            .build())
        .status(StatusInfo.builder()
            .currentStreak(stats.getCurrentStreak())
            .bestStreak(stats.getBestStreak())
            .completedQuests(stats.getCompletedQuests())
            .totalXp(stats.getTotalXp())
            .build())
        .skills(skills)
        .quests(quests)
        .inventory(inventory)
        .rewards(rewards)
        .statistics(statistics)
        .build();
  }

  private StatisticsDto calculateStatistics(UUID characterId) {
    Instant now = Instant.now();
    ZonedDateTime nowUtc = now.atZone(ZoneOffset.UTC);
    Instant startOfMonth = nowUtc.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS).toInstant();
    Instant startOfYear = nowUtc.withDayOfYear(1).truncatedTo(ChronoUnit.DAYS).toInstant();

    long completedThisMonth =
        questCompletionRepository.countByCharacterIdAndCompletedAtGreaterThanEqualAndCompletedAtLessThanEqual(
            characterId,
            startOfMonth,
            now
        );
    long completedThisYear =
        questCompletionRepository.countByCharacterIdAndCompletedAtGreaterThanEqualAndCompletedAtLessThanEqual(
            characterId,
            startOfYear,
            now
        );

    return StatisticsDto.builder()
        .completedThisYear(completedThisYear)
        .completedThisMonth(completedThisMonth)
        .build();
  }

  private QuestDifficulty parseDifficulty(String difficulty) {
    return difficulty == null ? null : QuestDifficulty.valueOf(difficulty);
  }

  private QuestType parseType(String type) {
    return type == null ? null : QuestType.valueOf(type);
  }
}
