package com.liferpg.controller;

import com.liferpg.config.AuthenticatedUserPrincipal;
import com.liferpg.dto.response.QuestCompleteDTO;
import com.liferpg.exception.UnauthorizedException;
import com.liferpg.service.quest.IQuestService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quest")
@RequiredArgsConstructor
public class QuestController {

  private final IQuestService questService;

  @PostMapping("/complete")
  public ResponseEntity<QuestCompleteDTO> questComplete(
      @RequestParam(name = "characterQuestId") UUID characterQuestId,
      Authentication authentication
  ) {
    UUID userId = extractUserId(authentication);
    QuestCompleteDTO result = questService.completeQuest(characterQuestId, userId);

    return ResponseEntity.ok(result);
  }

  private UUID extractUserId(Authentication authentication) {
    if (authentication == null
        || !(authentication.getPrincipal() instanceof AuthenticatedUserPrincipal principal)) {
      throw new UnauthorizedException("Unauthorized");
    }
    return principal.getUserId();
  }
}
