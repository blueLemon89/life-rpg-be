package com.liferpg.repository;

import com.liferpg.entity.LevelConfigEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelConfigRepository extends JpaRepository<LevelConfigEntity, UUID> {

  List<LevelConfigEntity> findAllByOrderByLevelAsc();
}
