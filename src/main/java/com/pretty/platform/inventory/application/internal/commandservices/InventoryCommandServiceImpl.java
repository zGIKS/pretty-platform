package com.pretty.platform.inventory.application.internal.commandservices;

import com.pretty.platform.inventory.application.internal.outboundservices.acl.ExternalProductService;
import com.pretty.platform.inventory.domain.model.commands.*;
import com.pretty.platform.inventory.domain.services.InventoryCommandService;
import com.pretty.platform.inventory.infrastructure.persistence.jpa.repositories.InventoryRepository;
import com.pretty.platform.shared.domain.model.aggregates.Inventory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of InventoryCommandService
 */
@Service
public class InventoryCommandServiceImpl implements InventoryCommandService {

    private final InventoryRepository inventoryRepository;
    private final ExternalProductService externalProductService;

    public InventoryCommandServiceImpl(InventoryRepository inventoryRepository,
                                     ExternalProductService externalProductService) {
        this.inventoryRepository = inventoryRepository;
        this.externalProductService = externalProductService;
    }

    @Override
    @Transactional
    public Optional<Long> handle(CreateInventoryCommand command) {
        // Verify product exists via ACL
        var productExists = externalProductService.fetchProductById(command.productId());
        if (productExists.isEmpty()) {
            throw new IllegalArgumentException("Product does not exist");
        }

        // Check if inventory already exists
        var existing = inventoryRepository.findByProductId_ProductIdAndVariant_Value(
            command.productId().productId(), command.variant().value());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Inventory already exists for this product variant");
        }

        var inventory = new Inventory(command.productId(), command.variant(),
                                    command.initialStock(), command.lowStockThreshold());
        var saved = inventoryRepository.save(inventory);
        return Optional.of(saved.getId());
    }

    @Override
    @Transactional
    public Optional<Long> handle(UpdateStockCommand command) {
        var inventory = inventoryRepository.findByProductId_ProductIdAndVariant_Value(
            command.productId().productId(), command.variant().value());
        if (inventory.isEmpty()) {
            return Optional.empty();
        }

        inventory.get().updateStock(command.availableStock(), command.reservedStock(), command.soldStock());
        inventoryRepository.save(inventory.get());
        return Optional.of(inventory.get().getId());
    }

    @Override
    @Transactional
    public Optional<Long> handle(ReserveStockCommand command) {
        var inventory = inventoryRepository.findByProductId_ProductIdAndVariant_Value(
            command.productId().productId(), command.variant().value());
        if (inventory.isEmpty()) {
            return Optional.empty();
        }

        inventory.get().reserveStock(command.quantity());
        inventoryRepository.save(inventory.get());
        return Optional.of(inventory.get().getId());
    }

    @Override
    @Transactional
    public Optional<Long> handle(ReleaseStockCommand command) {
        var inventory = inventoryRepository.findByProductId_ProductIdAndVariant_Value(
            command.productId().productId(), command.variant().value());
        if (inventory.isEmpty()) {
            return Optional.empty();
        }

        inventory.get().releaseStock(command.quantity());
        inventoryRepository.save(inventory.get());
        return Optional.of(inventory.get().getId());
    }

    @Override
    @Transactional
    public Optional<Long> handle(SellStockCommand command) {
        var inventory = inventoryRepository.findByProductId_ProductIdAndVariant_Value(
            command.productId().productId(), command.variant().value());
        if (inventory.isEmpty()) {
            return Optional.empty();
        }

        inventory.get().sellStock(command.quantity());
        inventoryRepository.save(inventory.get());
        return Optional.of(inventory.get().getId());
    }
}