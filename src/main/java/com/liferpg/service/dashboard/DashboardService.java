package com.liferpg.service.dashboard;

import com.liferpg.dto.response.dashboard.DashboardResponse;
import java.util.UUID;

public interface DashboardService {

  DashboardResponse getDashboard(UUID characterId);
}
