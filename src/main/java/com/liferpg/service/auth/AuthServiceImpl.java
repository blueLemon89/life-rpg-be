package com.liferpg.service.auth;

import java.util.Locale;
import java.util.Optional;

import com.liferpg.dto.request.RegisterRequest;
import com.liferpg.service.character.ICharacterSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthServiceImpl implements IAuthService {

  private final UserRepository userRepository;
  private final UserProfileRepository userProfileRepository;
  private final ICharacterSerivce characterSerivce;
  private final PasswordEncoder passwordEncoder;

  /**
   * Registers a new user and default profile.
   */
  @Override
  @Transactional
  public AuthResult register(RegisterRequest request) {
    String email = request.getEmail();
    String name = request.getName();
    String password = request.getPassword();
    String normalizedEmail = normalizeEmail(email);
    Optional<User> existedUser = userRepository.findByEmail(normalizedEmail);

    if (existedUser.isPresent()) {
      throw new ConflictException("Email already exists");
    }

    log.info("[Register] Start build user account");
    User user = User.builder()
        .email(normalizedEmail)
        .passwordHash(passwordEncoder.encode(password))
        .build();
    userRepository.save(user);

    log.info("[Register] Start build user profile");
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

    characterSerivce.initStarterCharacter(user, name);

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
