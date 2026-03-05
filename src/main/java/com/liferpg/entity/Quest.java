package com.liferpg.entity;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
  * Quest entity.
   */
@Entity
@Table(name = "quests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quest extends AuditEntity {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private QuestDifficulty difficulty;

  @Column(name = "xp_reward", nullable = false)
  private Integer xpReward;

  @Column(name = "gold_reward")
  private Integer goldReward;

  @Column(name = "skill_id")
  private UUID skillId;

  @Enumerated(EnumType.STRING)
  @Column(length = 50)
  private QuestType type;

  @Column(name = "unlock_level")
  private Integer unlockLevel;

  @PrePersist
  public void onCreate() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    if (goldReward == null) {
      goldReward = 0;
    }
    if (unlockLevel == null) {
      unlockLevel = 1;
    }
  }
}
