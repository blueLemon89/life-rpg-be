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
  * Game character entity.
   */
@Entity
@Table(name = "characters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Character extends AuditEntity {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(name = "user_id", nullable = false, unique = true)
  private UUID userId;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false)
  private Integer level;

  @Column(nullable = false)
  private Integer xp;

  @Column(nullable = false)
  private Integer gold;

  @Column(nullable = false)
  private Integer hp;

  @Column(name = "max_hp", nullable = false)
  private Integer maxHp;

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
    if (gold == null) {
      gold = 0;
    }
    if (hp == null) {
      hp = 100;
    }
    if (maxHp == null) {
      maxHp = 100;
    }
  }
}
