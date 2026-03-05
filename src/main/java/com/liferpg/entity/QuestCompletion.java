package com.liferpg.entity;

import java.time.Instant;
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
  * Quest completion log entity.
   */
@Entity
@Table(name = "quest_completions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestCompletion extends AuditEntity {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(name = "quest_id", nullable = false)
  private UUID questId;

  @Column(name = "character_id", nullable = false)
  private UUID characterId;

  @Column(name = "completed_at", nullable = false)
  private Instant completedAt;

  @Column(name = "xp_earned", nullable = false)
  private Integer xpEarned;

  @Column(name = "gold_earned", nullable = false)
  private Integer goldEarned;

  @PrePersist
  public void onCreate() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    if (completedAt == null) {
      completedAt = Instant.now();
    }
  }
}
