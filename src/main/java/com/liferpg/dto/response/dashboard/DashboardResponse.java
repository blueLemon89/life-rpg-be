package com.liferpg.dto.response.dashboard;

import java.util.List;
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
public class DashboardResponse {

  private CharacterInfo character;
  private StatusInfo status;
  private List<SkillDto> skills;
  private List<QuestDto> quests;
  private List<ItemDto> inventory;
  private List<RewardDto> rewards;
  private StatisticsDto statistics;
}
