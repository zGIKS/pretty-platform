package com.pretty.platform.inventory.domain.model.commands;

import com.pretty.platform.inventory.domain.model.valueobjects.ProductId;
import com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity;
import com.pretty.platform.inventory.domain.model.valueobjects.Threshold;
import com.pretty.platform.inventory.domain.model.valueobjects.Variant;

/**
 * Command to create inventory for a product
 */
public record CreateInventoryCommand(
    ProductId productId,
    Variant variant,
    StockQuantity initialStock,
    Threshold lowStockThreshold
) {
    public CreateInventoryCommand {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (variant == null) {
            throw new IllegalArgumentException("Variant cannot be null");
        }
        if (initialStock == null) {
            throw new IllegalArgumentException("Initial stock cannot be null");
        }
        if (lowStockThreshold == null) {
            throw new IllegalArgumentException("Low stock threshold cannot be null");
        }
    }
}