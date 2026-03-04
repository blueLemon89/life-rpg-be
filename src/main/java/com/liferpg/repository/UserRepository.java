package com.liferpg.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.liferpg.entity.User;

/**
  * Data access for users.
   */
public interface UserRepository extends JpaRepository<User, UUID> {

  /**
    * Finds user by email.
     */
  Optional<User> findByEmail(String email);
}
