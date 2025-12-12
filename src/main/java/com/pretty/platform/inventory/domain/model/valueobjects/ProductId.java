package com.pretty.platform.inventory.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.util.UUID;

/**
 * Value object representing Product ID from catalog context
 */
@Embeddable
public record ProductId(UUID productId) {
    public ProductId {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
    }
}