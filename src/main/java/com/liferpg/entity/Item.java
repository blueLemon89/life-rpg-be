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
  * Item master data entity.
   */
@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends AuditEntity {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(unique = true, length = 50)
  private String code;

  @Column(length = 100)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(length = 50)
  private String type;

  @Column(name = "xp_boost")
  private Integer xpBoost;

  @Column(name = "skill_boost")
  private Integer skillBoost;

  @PrePersist
  public void onCreate() {
    if (id == null) {
      id = UUID.randomUUID();
    }
  }
}
