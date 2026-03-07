package com.liferpg.repository;

import com.liferpg.entity.QuestCompletion;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestCompletionRepository extends JpaRepository<QuestCompletion, UUID> {

  long countByCharacterIdAndCompletedAtGreaterThanEqualAndCompletedAtLessThanEqual(
      UUID characterId,
      Instant completedAtFrom,
      Instant completedAtTo
  );
}
