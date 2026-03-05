package com.liferpg.entity;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
  * Character aggregated stats entity.
   */
@Entity
@Table(name = "character_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterStats extends AuditEntity {

  @Id
  @Column(name = "character_id", nullable = false, updatable = false)
  private UUID characterId;

  @Column(name = "current_streak")
  private Integer currentStreak;

  @Column(name = "best_streak")
  private Integer bestStreak;

  @Column(name = "completed_quests")
  private Integer completedQuests;

  @Column(name = "total_xp")
  private Integer totalXp;

  @PrePersist
  public void onCreate() {
    if (currentStreak == null) {
      currentStreak = 0;
    }
    if (bestStreak == null) {
      bestStreak = 0;
    }
    if (completedQuests == null) {
      completedQuests = 0;
    }
    if (totalXp == null) {
      totalXp = 0;
    }
  }
}
