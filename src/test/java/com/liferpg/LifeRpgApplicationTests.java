package com.liferpg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.liferpg.entity.Gender;
import com.liferpg.entity.User;
import com.liferpg.entity.UserProfile;
import com.liferpg.exception.UnauthorizedException;
import com.liferpg.repository.UserProfileRepository;
import com.liferpg.repository.UserRepository;
import com.liferpg.service.auth.AuthResult;
import com.liferpg.service.auth.AuthServiceImpl;
import com.liferpg.service.auth.IMeService;
import com.liferpg.service.auth.MeServiceImpl;

@ExtendWith(MockitoExtension.class)
class LifeRpgApplicationTests {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserProfileRepository userProfileRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthServiceImpl authServiceImpl;

  private final IMeService meService = new MeServiceImpl();

  @Test
  void registerSuccess() {
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
    when(passwordEncoder.encode("secret123")).thenReturn("hashed-password");
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
      User user = invocation.getArgument(0);
      user.setId(UUID.randomUUID());
      return user;
    });
    when(userProfileRepository.save(any(UserProfile.class)))
    .thenAnswer(invocation -> invocation.getArgument(0));

    AuthResult result = authServiceImpl.register("test@example.com", "secret123", "Hero");

    assertNotNull(result.getUser().getId());
    assertEquals("test@example.com", result.getUser().getEmail());
    assertEquals("hashed-password", result.getUser().getPasswordHash());
    assertEquals("Hero", result.getProfile().getName());
    assertEquals(Gender.OTHER, result.getProfile().getGender());
    assertEquals("pixel1", result.getProfile().getAvatarPresetId());
    assertEquals("Adventurer", result.getProfile().getTitle());
  }

  @Test
  void loginInvalidPasswordReturns401() {
    User user = User.builder()
    .id(UUID.randomUUID())
    .email("test@example.com")
    .passwordHash("hashed-password")
    .build();
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(eq("wrong-password"), eq("hashed-password"))).thenReturn(false);

    assertThrows(
    UnauthorizedException.class,
    () -> authServiceImpl.login("test@example.com", "wrong-password"));
  }

  @Test
  void meWithoutCookieReturns401() {
    assertThrows(UnauthorizedException.class, () -> meService.me(null));
  }
}
