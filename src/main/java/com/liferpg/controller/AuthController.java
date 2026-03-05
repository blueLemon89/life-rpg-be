package com.liferpg.controller;

import java.util.Map;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.liferpg.config.JwtService;
import com.liferpg.dto.request.LoginRequest;
import com.liferpg.dto.request.RegisterRequest;
import com.liferpg.dto.response.LoginResponse;
import com.liferpg.dto.response.RegisterResponse;
import com.liferpg.service.auth.AuthCookieService;
import com.liferpg.service.auth.IAuthService;

/**
  * Authentication endpoints: register, login, and logout.
   */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final IAuthService authService;
  private final JwtService jwtService;
  private final AuthCookieService authCookieService;

  /**
    * Registers a new user and issues a session cookie.
     */
  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {

    log.info("[Register] Start register new user");
    var result = authService.register(request);
    String token = jwtService.sign(
        result.getUser().getId(),
        result.getUser().getEmail(),
        result.getProfile().getName());

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, authCookieService.buildSessionCookie(token))
        .body(new RegisterResponse(result.getUser().getId()));
  }

  /**
    * Authenticates a user and issues a session cookie.
     */
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    var result = authService.login(request.getEmail(), request.getPassword());
    String token = jwtService.sign(
        result.getUser().getId(),
        result.getUser().getEmail(),
        result.getProfile().getName());

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, authCookieService.buildSessionCookie(token))
        .body(new LoginResponse(result.getUser().getId()));
  }

  /**
    * Clears the session cookie.
     */
  @PostMapping("/logout")
  public ResponseEntity<Map<String, Boolean>> logout() {
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, authCookieService.clearSessionCookie())
        .body(Map.of("ok", true));
  }
}
