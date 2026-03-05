package com.liferpg.service.quest;

import java.util.List;
import java.util.UUID;
import com.liferpg.entity.CharacterQuest;

public interface IQuestService {

    void generateStarterQuests(UUID characterId);

    void completeQuest(UUID characterQuestId);

    List<CharacterQuest> getActiveQuests(UUID characterId);
}
