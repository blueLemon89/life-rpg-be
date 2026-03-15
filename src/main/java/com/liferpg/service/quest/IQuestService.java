package com.liferpg.service.quest;

import java.util.List;
import java.util.UUID;

import com.liferpg.dto.response.QuestCompleteDTO;
import com.liferpg.entity.CharacterQuest;

public interface IQuestService {

    void generateStarterQuests(UUID characterId);

    QuestCompleteDTO completeQuest(UUID characterQuestId, UUID userId);

    List<CharacterQuest> getActiveQuests(UUID characterId);

    void refeshQuest(UUID characterID);
}
