package com.liferpg.service.skills.impl;

import com.liferpg.entity.Character;
import com.liferpg.entity.CharacterSkill;
import com.liferpg.entity.Skill;
import com.liferpg.repository.CharacterSkillRepository;
import com.liferpg.repository.SkillRepository;
import com.liferpg.service.skills.ISkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillServiceImpl implements ISkillService {

    private final SkillRepository skillRepository;
    private final CharacterSkillRepository characterSkillRepository;

    @Override
    public void initSkills(Character character) {
        List<Skill> skills = skillRepository.findAll();

        for (Skill skill : skills) {
            CharacterSkill cs = CharacterSkill.builder()
                    .id(UUID.randomUUID())
                    .characterId(character.getId())
                    .skillId(skill.getId())
                    .level(1)
                    .xp(0)
                    .build();

            characterSkillRepository.save(cs);
        }
    }
}
