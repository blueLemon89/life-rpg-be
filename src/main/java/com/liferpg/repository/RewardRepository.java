package com.liferpg.repository;

import com.liferpg.entity.Reward;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<Reward, UUID> {

  List<Reward> findByCharacterIdOrderByCreatedAtDesc(UUID characterId);
}
