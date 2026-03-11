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
public class CharacterInfo {

  private String name;
  private Integer level;
  private Integer xp;
  private Integer gold;
  private Integer hp;
  private Integer maxHp;
}
