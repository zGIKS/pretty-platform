package com.pretty.platform.catalog.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object for product subcategory
 */
@Embeddable
public record Subcategory(String name) {
    public Subcategory {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Subcategory name cannot be null or empty");
        }
    }
}