package com.liferpg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.liferpg.config.AuthenticatedUserPrincipal;
import com.liferpg.controller.QuestController;
import com.liferpg.dto.response.QuestCompleteDTO;
import com.liferpg.dto.response.dashboard.DashboardResponse;
import com.liferpg.entity.Character;
import com.liferpg.entity.CharacterQuest;
import com.liferpg.entity.CharacterQuestStatus;
import com.liferpg.entity.CharacterStats;
import com.liferpg.entity.LevelConfigEntity;
import com.liferpg.entity.Quest;
import com.liferpg.repository.CharacterQuestRepository;
import com.liferpg.repository.CharacterRepository;
import com.liferpg.repository.CharacterSkillRepository;
import com.liferpg.repository.CharacterStatsRepository;
import com.liferpg.repository.InventoryRepository;
import com.liferpg.repository.ItemRepository;
import com.liferpg.repository.LevelConfigRepository;
import com.liferpg.repository.QuestCompletionRepository;
import com.liferpg.repository.QuestRepository;
import com.liferpg.repository.RewardRepository;
import com.liferpg.service.dashboard.impl.DashboardServiceImpl;
import com.liferpg.service.quest.impl.QuestServiceImpl;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class QuestCompletionFlowTests {

  @Mock
  private CharacterRepository characterRepository;
  @Mock
  private CharacterStatsRepository characterStatsRepository;
  @Mock
  private CharacterSkillRepository characterSkillRepository;
  @Mock
  private CharacterQuestRepository characterQuestRepository;
  @Mock
  private InventoryRepository inventoryRepository;
  @Mock
  private RewardRepository rewardRepository;
  @Mock
  private QuestCompletionRepository questCompletionRepository;
  @Mock
  private QuestRepository questRepository;
  @Mock
  private ItemRepository itemRepository;
  @Mock
  private LevelConfigRepository levelConfigRepository;

  @InjectMocks
  private DashboardServiceImpl dashboardService;

  @InjectMocks
  private QuestServiceImpl questService;

  private QuestController questController;

  @BeforeEach
  void setUp() {
    questController = new QuestController(questService);
  }

  @Test
  void dashboardPayloadContainsCharacterQuestIdAndQuestId() {
    UUID characterId = UUID.randomUUID();
    UUID characterQuestId = UUID.randomUUID();
    UUID questId = UUID.randomUUID();

    Character character = Character.builder()
        .id(characterId)
        .name("Hero")
        .level(1)
        .xp(0)
        .gold(0)
        .hp(100)
        .maxHp(100)
        .build();

    CharacterStats stats = CharacterStats.builder()
        .characterId(characterId)
        .currentStreak(0)
        .bestStreak(0)
        .completedQuests(0)
        .totalXp(0)
        .build();

    when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
    when(characterStatsRepository.findById(characterId)).thenReturn(Optional.of(stats));
    when(characterSkillRepository.findDashboardSkillsByCharacterId(characterId))
        .thenReturn(Collections.emptyList());
    when(characterQuestRepository.findDashboardQuestsByCharacterIdAndStatus(
        characterId,
        CharacterQuestStatus.ACTIVE.name()
    )).thenReturn(List.of(activeQuestProjection(characterQuestId, questId)));
    when(inventoryRepository.findDashboardInventoryByCharacterId(characterId))
        .thenReturn(Collections.emptyList());
    when(rewardRepository.findByCharacterIdOrderByCreatedAtDesc(characterId))
        .thenReturn(Collections.emptyList());
    when(questCompletionRepository
        .countByCharacterIdAndCompletedAtGreaterThanEqualAndCompletedAtLessThanEqual(
            eq(characterId), any(Instant.class), any(Instant.class)))
        .thenReturn(0L);

    DashboardResponse dashboard = dashboardService.getDashboard(characterId);

    assertEquals(1, dashboard.getQuests().size());
    assertEquals(characterQuestId, dashboard.getQuests().getFirst().getCharacterQuestId());
    assertEquals(questId, dashboard.getQuests().getFirst().getQuestId());
  }

  @Test
  void completeQuestUsingCharacterQuestIdFromDashboardReturns200() {
    UUID userId = UUID.randomUUID();
    UUID characterId = UUID.randomUUID();
    UUID characterQuestId = UUID.randomUUID();
    UUID questId = UUID.randomUUID();

    Character character = Character.builder()
        .id(characterId)
        .userId(userId)
        .name("Hero")
        .level(1)
        .xp(0)
        .gold(10)
        .hp(100)
        .maxHp(100)
        .build();
    CharacterStats stats = CharacterStats.builder()
        .characterId(characterId)
        .currentStreak(0)
        .bestStreak(0)
        .completedQuests(0)
        .totalXp(0)
        .build();
    CharacterQuest characterQuest = CharacterQuest.builder()
        .id(characterQuestId)
        .characterId(characterId)
        .questId(questId)
        .status(CharacterQuestStatus.ACTIVE)
        .assignedAt(Instant.now())
        .build();
    Quest questTemplate = Quest.builder()
        .id(questId)
        .title("Quest")
        .xpReward(25)
        .goldReward(5)
        .build();
    LevelConfigEntity levelConfig = LevelConfigEntity.builder()
        .id(UUID.randomUUID())
        .level(1)
        .xpRequired(100)
        .build();

    when(characterRepository.findById(characterId)).thenReturn(Optional.of(character));
    when(characterStatsRepository.findById(characterId)).thenReturn(Optional.of(stats));
    when(characterSkillRepository.findDashboardSkillsByCharacterId(characterId))
        .thenReturn(Collections.emptyList());
    when(characterQuestRepository.findDashboardQuestsByCharacterIdAndStatus(
        characterId,
        CharacterQuestStatus.ACTIVE.name()
    )).thenReturn(List.of(activeQuestProjection(characterQuestId, questId)));
    when(inventoryRepository.findDashboardInventoryByCharacterId(characterId))
        .thenReturn(Collections.emptyList());
    when(rewardRepository.findByCharacterIdOrderByCreatedAtDesc(characterId))
        .thenReturn(Collections.emptyList());
    when(questCompletionRepository
        .countByCharacterIdAndCompletedAtGreaterThanEqualAndCompletedAtLessThanEqual(
            eq(characterId), any(Instant.class), any(Instant.class)))
        .thenReturn(0L);

    when(characterRepository.findByUserId(userId)).thenReturn(Optional.of(character));
    when(characterQuestRepository.findByIdAndCharacterId(characterQuestId, characterId))
        .thenReturn(Optional.of(characterQuest));
    when(questRepository.findById(questId)).thenReturn(Optional.of(questTemplate));
    when(levelConfigRepository.findAllByOrderByLevelAsc()).thenReturn(List.of(levelConfig));
    when(characterRepository.save(any(Character.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(characterQuestRepository.save(any(CharacterQuest.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(characterStatsRepository.save(any(CharacterStats.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(questCompletionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    DashboardResponse dashboard = dashboardService.getDashboard(characterId);
    UUID idFromDashboard = dashboard.getQuests().getFirst().getCharacterQuestId();
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        new AuthenticatedUserPrincipal(userId, "hero@example.com", "Hero"),
        null,
        List.of()
    );

    ResponseEntity<QuestCompleteDTO> response = questController.questComplete(
        idFromDashboard,
        authentication
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(Boolean.TRUE.equals(response.getBody().getQuestCompleted()));
    verify(characterQuestRepository).findByIdAndCharacterId(idFromDashboard, characterId);
  }

  private CharacterQuestRepository.ActiveQuestProjection activeQuestProjection(
      UUID characterQuestId,
      UUID questId
  ) {
    return new CharacterQuestRepository.ActiveQuestProjection() {
      @Override
      public UUID getCharacterQuestId() {
        return characterQuestId;
      }

      @Override
      public UUID getQuestId() {
        return questId;
      }

      @Override
      public String getTitle() {
        return "Quest";
      }

      @Override
      public String getDifficulty() {
        return "EASY";
      }

      @Override
      public Integer getXpReward() {
        return 25;
      }

      @Override
      public String getType() {
        return "DAILY";
      }
    };
  }
}
