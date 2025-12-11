package com.pretty.platform.inventory.application.internal.queryservices;

import com.pretty.platform.inventory.domain.model.queries.*;
import com.pretty.platform.inventory.domain.services.InventoryQueryService;
import com.pretty.platform.inventory.infrastructure.persistence.jpa.repositories.InventoryRepository;
import com.pretty.platform.shared.domain.model.aggregates.Inventory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of InventoryQueryService
 */
@Service
@Transactional(readOnly = true)
public class InventoryQueryServiceImpl implements InventoryQueryService {

    private final InventoryRepository inventoryRepository;

    public InventoryQueryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Optional<Inventory> handle(GetInventoryByProductQuery query) {
        return inventoryRepository.findByProductId_ProductId(query.productId().productId()).stream().findFirst();
    }

    @Override
    public List<Inventory> handle(GetAllInventoryQuery query) {
        return inventoryRepository.findAll();
    }

    @Override
    public List<Inventory> handle(GetLowStockItemsQuery query) {
        return inventoryRepository.findLowStockItems();
    }
}