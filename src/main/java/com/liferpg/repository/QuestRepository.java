package com.liferpg.repository;

import com.liferpg.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestRepository extends JpaRepository<Quest, UUID> {

    @Query(
        value = """
            SELECT *
            FROM quests
            WHERE difficulty = 'EASY'
              AND unlock_level <= :unlockLevel
            ORDER BY RANDOM()
            LIMIT 3
            """,
        nativeQuery = true
    )
    List<Quest> findStarterEasyQuests(@Param("unlockLevel") int unlockLevel);

    @Query(value = "SELECT * from quests where difficulty = :difficult ORDER BY RANDOM() LIMIT 3", nativeQuery = true)
    List<Quest> generateQuestDailyForCharacter(String difficult);
}
