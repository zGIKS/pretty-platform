package com.pretty.platform.catalog.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object for product category
 */
@Embeddable
public record Category(String name) {
    public Category {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
    }
}