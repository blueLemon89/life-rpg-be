package com.liferpg.service.character.impl;

import com.liferpg.entity.Character;
import com.liferpg.entity.User;
import com.liferpg.repository.CharacterRepository;
import com.liferpg.service.character.ICharacterInitializer;
import com.liferpg.service.character.ICharacterSerivce;
import com.liferpg.service.quest.IQuestService;
import com.liferpg.service.skills.ISkillService;
import com.liferpg.service.stat.IStatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static com.liferpg.common.Constant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterServiceImpl implements ICharacterSerivce {

    private final CharacterRepository characterRepository;
    private final ICharacterInitializer characterInitializer;

    @Override
    @Transactional
    public void initStarterCharacter(User user, String name) {

        log.info("[Character] Start init new character for user {}", name);
        Character character = Character.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .name(name)
                .level(INIT_LEVEL)
                .xp(INIT_XP)
                .gold(INIT_GOLD)
                .hp(INIT_HP)
                .maxHp(INIT_MAX_HP)
                .build();

        characterRepository.save(character);

        // init systems
        initSystem(character);
    }


    private void initSystem(Character character) {
        characterInitializer.initialize(character);
    }

}
