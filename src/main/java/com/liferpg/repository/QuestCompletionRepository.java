package com.liferpg.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.liferpg.entity.QuestCompletion;

@Repository
public interface QuestCompletionRepository extends JpaRepository<QuestCompletion, UUID> {
}
