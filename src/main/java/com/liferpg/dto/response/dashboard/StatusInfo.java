package com.liferpg.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusInfo {

  private Integer currentStreak;
  private Integer bestStreak;
  private Integer completedQuests;
  private Integer totalXp;
}
