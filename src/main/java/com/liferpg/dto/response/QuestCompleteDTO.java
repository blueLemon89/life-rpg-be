package com.liferpg.dto.response;

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
public class QuestCompleteDTO {
  private Boolean questCompleted;
  private Integer xpGained;
  private Integer goldGained;
  private Boolean levelUp;
  private Integer oldLevel;
  private Integer newLevel;
  private Integer currentExp;
  private Integer expToNextLevel;
  private UUID rewardItemId;
  private String rewardItemName;
  private Integer rewardQuantity;
}
