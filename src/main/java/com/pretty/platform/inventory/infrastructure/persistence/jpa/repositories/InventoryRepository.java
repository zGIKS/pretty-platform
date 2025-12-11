package com.pretty.platform.inventory.infrastructure.persistence.jpa.repositories;

import com.pretty.platform.inventory.domain.model.valueobjects.ProductId;
import com.pretty.platform.shared.domain.model.aggregates.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Inventory aggregate
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId_ProductIdAndVariant_Value(UUID productId, String variant);

    @Query("SELECT i FROM Inventory i WHERE i.availableStock.value <= i.lowStockThreshold.value")
    List<Inventory> findLowStockItems();

    List<Inventory> findByProductId_ProductId(UUID productId);
}