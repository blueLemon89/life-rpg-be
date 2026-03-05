package com.liferpg.repository;

import com.liferpg.entity.CharacterSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CharacterSkillRepository extends JpaRepository<CharacterSkill, UUID> {
}
