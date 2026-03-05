package com.liferpg.entity;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
  * Character skill progression entity.
   */
@Entity
@Table(
    name = "character_skills",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_character_skills_character_skill", columnNames = {"character_id", "skill_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterSkill extends AuditEntity {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(name = "character_id", nullable = false)
  private UUID characterId;

  @Column(name = "skill_id", nullable = false)
  private UUID skillId;

  @Column(nullable = false)
  private Integer level;

  @Column(nullable = false)
  private Integer xp;

  @PrePersist
  public void onCreate() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    if (level == null) {
      level = 1;
    }
    if (xp == null) {
      xp = 0;
    }
  }
}
