package com.liferpg.repository;

import com.liferpg.entity.Item;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
}
