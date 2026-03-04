package com.liferpg.service;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.liferpg.entity.Gender;
import com.liferpg.entity.User;
import com.liferpg.entity.UserProfile;
import com.liferpg.exception.BadRequestException;
import com.liferpg.exception.ConflictException;
import com.liferpg.exception.UnauthorizedException;
import com.liferpg.repository.UserProfileRepository;
import com.liferpg.repository.UserRepository;

/**
  * Authentication business logic.
   */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final UserProfileRepository userProfileRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Registers a new user and default profile.
   */
  @Override
  @Transactional
  public AuthResult register(String email, String password, String name) {
    String normalizedEmail = normalizeEmail(email);
    if (userRepository.findByEmail(normalizedEmail).isPresent()) {
      throw new ConflictException("Email already exists");
    }

    User user = User.builder()
        .email(normalizedEmail)
        .passwordHash(passwordEncoder.encode(password))
        .build();
    userRepository.save(user);

    UserProfile profile = UserProfile.builder()
        .userId(user.getId())
        .name(name.trim())
        .gender(Gender.OTHER)
        .avatarType("preset")
        .avatarPresetId("pixel1")
        .avatarImageUrl(null)
        .title("Adventurer")
        .build();
    userProfileRepository.save(profile);

    return new AuthResult(user, profile);
  }

  /**
   * Authenticates user credentials.
   */
  @Override
  @Transactional(readOnly = true)
  public AuthResult login(String email, String password) {
    String normalizedEmail = normalizeEmail(email);
    User user = userRepository.findByEmail(normalizedEmail)
        .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new UnauthorizedException("Invalid credentials");
    }

    UserProfile profile = userProfileRepository.findById(user.getId())
        .orElseThrow(() -> new BadRequestException("User profile not found"));

    return new AuthResult(user, profile);
  }

  private String normalizeEmail(String email) {
    return email.trim().toLowerCase(Locale.ROOT);
  }
}
