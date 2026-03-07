package com.liferpg.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.liferpg.config.AuthenticatedUserPrincipal;
import com.liferpg.dto.response.dashboard.DashboardResponse;
import com.liferpg.entity.Character;
import com.liferpg.exception.BadRequestException;
import com.liferpg.exception.UnauthorizedException;
import com.liferpg.repository.CharacterRepository;
import com.liferpg.service.dashboard.DashboardService;

@RequestMapping({"/api/dashboard", "/api/v1/dashboard"})
@RestController
@RequiredArgsConstructor
public class DashboardController {

  private final DashboardService dashboardService;
  private final CharacterRepository characterRepository;

  @GetMapping
  public DashboardResponse getDashboard(Authentication authentication) {
    UUID userId = extractUserId(authentication);
    UUID characterId = characterRepository.findByUserId(userId)
        .map(Character::getId)
        .orElseThrow(() -> new BadRequestException("Character not found"));
    return dashboardService.getDashboard(characterId);
  }

  private UUID extractUserId(Authentication authentication) {
    if (authentication == null
        || !(authentication.getPrincipal() instanceof AuthenticatedUserPrincipal principal)) {
      throw new UnauthorizedException("Unauthorized");
    }
    return principal.getUserId();
  }
}
