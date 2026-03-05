package com.liferpg.service.character.impl;

import com.liferpg.entity.Character;
import com.liferpg.service.character.ICharacterInitializer;
import com.liferpg.service.quest.IQuestService;
import com.liferpg.service.skills.ISkillService;
import com.liferpg.service.stat.IStatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterInitializerImpl implements ICharacterInitializer {

    private final IQuestService questService;
    private final IStatService statsService;
    private final ISkillService skillService;

    @Override
    public void initialize(Character character) {
        initQuest(character);
        initSkill(character);
        initStats(character);
    }

    private void initSkill(Character character) {
        skillService.initSkills(character);
    }

    private void initStats(Character character) {
        statsService.initStats(character);
    }

    private void initQuest(Character character) {
        questService.generateStarterQuests(character.getId());
    }
}
