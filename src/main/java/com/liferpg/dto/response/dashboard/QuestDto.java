package com.liferpg.dto.response.dashboard;

import com.liferpg.entity.QuestDifficulty;
import com.liferpg.entity.QuestType;
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
public class QuestDto {

  private UUID characterQuestId;
  private UUID questId;
  private String title;
  private QuestDifficulty difficulty;
  private Integer xpReward;
  private QuestType type;
}
