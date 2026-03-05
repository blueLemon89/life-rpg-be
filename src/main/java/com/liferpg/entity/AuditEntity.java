package com.liferpg.entity;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.liferpg.config.AuthenticatedUserPrincipal;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AuditEntity {

  private static final String SYSTEM_ACTOR = "SYSTEM";

  @Column(name = "created_by", nullable = false, updatable = false, length = 100)
  private String createdBy;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_by", nullable = false, length = 100)
  private String updatedBy;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @PrePersist
  public void auditOnCreate() {
    Instant now = Instant.now();
    String actor = resolveCurrentActor();

    if (createdAt == null) {
      createdAt = now;
    }
    if (createdBy == null || createdBy.isBlank()) {
      createdBy = actor;
    }

    updatedAt = now;
    updatedBy = actor;
  }

  @PreUpdate
  public void auditOnUpdate() {
    updatedAt = Instant.now();
    updatedBy = resolveCurrentActor();
  }

  private String resolveCurrentActor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return SYSTEM_ACTOR;
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof AuthenticatedUserPrincipal authenticatedUserPrincipal) {
      UUID userId = authenticatedUserPrincipal.getUserId();
      return userId != null ? userId.toString() : SYSTEM_ACTOR;
    }

    if (principal instanceof String principalName && !principalName.isBlank()
        && !"anonymousUser".equalsIgnoreCase(principalName)) {
      return principalName;
    }

    return SYSTEM_ACTOR;
  }
}
