package com.pretty.platform.catalog.domain.model.queries;

import java.util.UUID;

/**
 * Query to get a product by ID
 */
public record GetProductByIdQuery(UUID productId) {
    public GetProductByIdQuery {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }
    }
}