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
public class SkillDto {

  private String skillCode;
  private String skillName;
  private Integer level;
  private Integer xp;
}
