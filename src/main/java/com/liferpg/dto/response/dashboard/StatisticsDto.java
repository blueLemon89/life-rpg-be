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
public class StatisticsDto {

  private Long completedThisYear;
  private Long completedThisMonth;
}
