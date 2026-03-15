package com.liferpg.repository;

import com.liferpg.dto.QuestConfigDTO;
import com.liferpg.entity.QuestConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestConfigRepository extends JpaRepository<QuestConfigEntity, Long> {

    @Query(value = """
    select
        level_min,
        level_max,
        quest_difficult
    from
        quest_config
    where level_min <= :level
        and level_max >= :level
    """, nativeQuery = true)
    QuestConfigDTO getConfigQuestByCurrentLevel(@Param("level") int level);
}
