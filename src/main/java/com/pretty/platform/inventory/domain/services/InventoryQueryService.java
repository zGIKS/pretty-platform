package com.pretty.platform.inventory.domain.services;

import com.pretty.platform.inventory.domain.model.queries.GetAllInventoryQuery;
import com.pretty.platform.inventory.domain.model.queries.GetInventoryByProductQuery;
import com.pretty.platform.inventory.domain.model.queries.GetLowStockItemsQuery;
import com.pretty.platform.shared.domain.model.aggregates.Inventory;

import java.util.List;
import java.util.Optional;

/**
 * Domain service for inventory query operations
 */
public interface InventoryQueryService {
    Optional<Inventory> handle(GetInventoryByProductQuery query);
    List<Inventory> handle(GetAllInventoryQuery query);
    List<Inventory> handle(GetLowStockItemsQuery query);
}