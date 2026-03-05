package com.liferpg.service.stat.impl;

import com.liferpg.entity.Character;
import com.liferpg.entity.CharacterStats;
import com.liferpg.repository.CharacterStatsRepository;
import com.liferpg.service.stat.IStatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements IStatService {

    private final CharacterStatsRepository statsRepository;

    @Override
    public void initStats(Character character) {
        CharacterStats stats = CharacterStats.builder()
                .characterId(character.getId())
                .currentStreak(0)
                .bestStreak(0)
                .completedQuests(0)
                .totalXp(0)
                .build();

        statsRepository.save(stats);
    }
}
