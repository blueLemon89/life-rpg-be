package com.liferpg.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "level_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LevelConfigEntity extends AuditEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "level")
    private Integer level;

    @Column(name = "xp_required")
    private Integer xpRequired;

    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
