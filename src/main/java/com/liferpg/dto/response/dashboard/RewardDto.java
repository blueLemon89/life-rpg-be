package com.liferpg.dto.response.dashboard;

import java.util.UUID;
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
public class RewardDto {

  private UUID rewardId;
  private String name;
  private String description;
  private String unlockCondition;
  private Boolean isUnlocked;
}
