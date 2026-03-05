package com.liferpg.repository;

import com.liferpg.entity.CharacterStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CharacterStatsRepository extends JpaRepository<CharacterStats, UUID> {
}
