package com.pretty.platform.inventory.domain.model.commands;

import com.pretty.platform.inventory.domain.model.valueobjects.ProductId;
import com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity;
import com.pretty.platform.inventory.domain.model.valueobjects.Variant;

/**
 * Command to update stock levels
 */
public record UpdateStockCommand(
    ProductId productId,
    Variant variant,
    StockQuantity availableStock,
    StockQuantity reservedStock,
    StockQuantity soldStock
) {
    public UpdateStockCommand {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (variant == null) {
            throw new IllegalArgumentException("Variant cannot be null");
        }
        if (availableStock == null) {
            throw new IllegalArgumentException("Available stock cannot be null");
        }
        if (reservedStock == null) {
            throw new IllegalArgumentException("Reserved stock cannot be null");
        }
        if (soldStock == null) {
            throw new IllegalArgumentException("Sold stock cannot be null");
        }
    }
}