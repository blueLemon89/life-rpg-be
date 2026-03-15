package com.liferpg.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quest_config")
public class QuestConfigEntity extends AuditEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "level_min")
    private Integer levelMin;

    @Column(name = "level_max")
    private Integer levelMax;

    @Column(name = "quest_difficult")
    private String difficult;
}
