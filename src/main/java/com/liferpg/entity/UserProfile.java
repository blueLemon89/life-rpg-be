package com.liferpg.entity;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
  * User profile entity.
   */
@Entity
@Table(name = "user_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

  @Id
  @Column(name = "user_id", nullable = false, updatable = false)
  private UUID userId;

  @Column(nullable = false, length = 100)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Gender gender;

  @Column(name = "avatar_type", nullable = false, length = 50)
  private String avatarType;

  @Column(name = "avatar_preset_id", length = 100)
  private String avatarPresetId;

  @Column(name = "avatar_image_url", columnDefinition = "TEXT")
  private String avatarImageUrl;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  /**
    * Initializes timestamp before insert.
     */
  @PrePersist
  public void onCreate() {
    updatedAt = Instant.now();
  }

  /**
    * Updates timestamp before update.
     */
  @PreUpdate
  public void onUpdate() {
    updatedAt = Instant.now();
  }
}
