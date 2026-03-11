package com.liferpg.repository;

import com.liferpg.entity.CharacterQuest;
import com.liferpg.entity.CharacterQuestStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterQuestRepository extends JpaRepository<CharacterQuest, UUID> {

  Optional<CharacterQuest> findByIdAndCharacterId(UUID id, UUID characterId);

  List<CharacterQuest> findByCharacterIdAndStatusOrderByAssignedAtAsc(
      UUID characterId,
      CharacterQuestStatus status
  );

  @Query(value = """
      SELECT cq.id AS characterQuestId,
             q.id AS questId,
             q.title AS title,
             q.difficulty AS difficulty,
             q.xp_reward AS xpReward,
             q.type AS type
      FROM character_quests cq
      JOIN quests q ON q.id = cq.quest_id
      WHERE cq.character_id = :characterId
        AND cq.status = :status
      ORDER BY cq.assigned_at ASC
      """, nativeQuery = true)
  List<ActiveQuestProjection> findDashboardQuestsByCharacterIdAndStatus(
      @Param("characterId") UUID characterId,
      @Param("status") String status
  );

  interface ActiveQuestProjection {
    UUID getCharacterQuestId();

    UUID getQuestId();

    String getTitle();

    String getDifficulty();

    Integer getXpReward();

    String getType();
  }
}
