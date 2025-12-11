package com.pretty.platform.catalog.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object for product title
 */
@Embeddable
public record ProductTitle(String title) {
    public ProductTitle {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Product title cannot be null or empty");
        }
    }
}