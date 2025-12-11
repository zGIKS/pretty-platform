package com.pretty.platform.promotions.domain.model.queries;

import java.util.UUID;

/**
 * Query to get base price for a product
 */
public record GetBasePriceQuery(UUID productId) {
    public GetBasePriceQuery {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
    }
}