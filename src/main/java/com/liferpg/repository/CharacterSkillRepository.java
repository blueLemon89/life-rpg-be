package com.liferpg.repository;

import com.liferpg.entity.CharacterSkill;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterSkillRepository extends JpaRepository<CharacterSkill, UUID> {

  @Query(value = """
      SELECT s.code AS skillCode,
             s.name AS skillName,
             cs.level AS level,
             cs.xp AS xp
      FROM character_skills cs
      JOIN skills s ON s.id = cs.skill_id
      WHERE cs.character_id = :characterId
      ORDER BY s.name ASC
      """, nativeQuery = true)
  List<SkillProjection> findDashboardSkillsByCharacterId(@Param("characterId") UUID characterId);

  interface SkillProjection {
    String getSkillCode();

    String getSkillName();

    Integer getLevel();

    Integer getXp();
  }
}
