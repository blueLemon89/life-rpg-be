package com.liferpg.repository;

import com.liferpg.entity.CharacterQuest;
import com.liferpg.entity.CharacterQuestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CharacterQuestRepository extends JpaRepository<CharacterQuest, UUID> {

    List<CharacterQuest> findByCharacterIdAndStatusOrderByAssignedAtAsc(
        UUID characterId,
        CharacterQuestStatus status
    );
}
