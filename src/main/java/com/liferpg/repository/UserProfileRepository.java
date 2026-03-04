package com.liferpg.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.liferpg.entity.UserProfile;

/**
  * Data access for user profiles.
   */
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
}
