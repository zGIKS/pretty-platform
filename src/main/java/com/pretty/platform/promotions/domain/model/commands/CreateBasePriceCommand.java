package com.pretty.platform.promotions.domain.model.commands;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Command to create base price for a product
 */
public record CreateBasePriceCommand(
    UUID productId,
    BigDecimal price,
    String currency
) {
    public CreateBasePriceCommand {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
    }
}