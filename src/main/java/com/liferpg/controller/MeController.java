package com.liferpg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.liferpg.dto.response.MeResponse;
import com.liferpg.service.MeService;

/**
  * Endpoint exposing the current authenticated user.
   */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MeController {

  private final MeService meService;

  /**
    * Returns the authenticated user.
     */
  @GetMapping("/me")
  public MeResponse me(Authentication authentication) {
    return meService.me(authentication);
  }
}
