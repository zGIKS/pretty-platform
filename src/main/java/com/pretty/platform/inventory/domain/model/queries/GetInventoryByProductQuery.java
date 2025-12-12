package com.pretty.platform.inventory.domain.model.queries;

import com.pretty.platform.inventory.domain.model.valueobjects.ProductId;

/**
 * Query to get inventory by product ID
 */
public record GetInventoryByProductQuery(ProductId productId) {
    public GetInventoryByProductQuery {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
    }
}