package com.pretty.platform.inventory.domain.model.commands;

import com.pretty.platform.inventory.domain.model.valueobjects.ProductId;
import com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity;
import com.pretty.platform.inventory.domain.model.valueobjects.Variant;

/**
 * Command to mark stock as sold
 */
public record SellStockCommand(
    ProductId productId,
    Variant variant,
    StockQuantity quantity
) {
    public SellStockCommand {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (variant == null) {
            throw new IllegalArgumentException("Variant cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (quantity.isZero()) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}