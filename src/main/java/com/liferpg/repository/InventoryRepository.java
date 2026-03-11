package com.liferpg.repository;

import com.liferpg.entity.Inventory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

  Optional<Inventory> findByCharacterIdAndItemId(UUID characterId, UUID itemId);

  @Query(value = """
      SELECT i.id AS itemId,
             i.name AS name,
             inv.quantity AS quantity
      FROM inventories inv
      JOIN items i ON i.id = inv.item_id
      WHERE inv.character_id = :characterId
      ORDER BY i.name ASC
      """, nativeQuery = true)
  List<InventoryItemProjection> findDashboardInventoryByCharacterId(
      @Param("characterId") UUID characterId
  );

  interface InventoryItemProjection {
    UUID getItemId();

    String getName();

    Integer getQuantity();
  }
}
