package com.liferpg.entity;

import java.time.Instant;
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
  * Character quest assignment entity.
   */
@Entity
@Table(name = "character_quests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterQuest extends AuditEntity {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(name = "character_id", nullable = false)
  private UUID characterId;

  @Column(name = "quest_id", nullable = false)
  private UUID questId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private CharacterQuestStatus status;

  @Column(name = "assigned_at", nullable = false)
  private Instant assignedAt;

  @Column(name = "completed_at")
  private Instant completedAt;

  @PrePersist
  public void onCreate() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    if (status == null) {
      status = CharacterQuestStatus.ACTIVE;
    }
    if (assignedAt == null) {
      assignedAt = Instant.now();
    }
  }
}
