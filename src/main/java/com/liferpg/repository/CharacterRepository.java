package com.liferpg.repository;

import com.liferpg.entity.Character;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, UUID> {
  Optional<Character> findByUserId(UUID userId);

  @Query(value = """
    select
      level
    from
      characters
    where
      id = :characterId
   """, nativeQuery = true)
  int getLevelById(UUID characterId);
}
