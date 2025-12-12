package com.pretty.platform.inventory.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing product variant (e.g., size, color)
 */
@Embeddable
public record Variant(String value) {
    public Variant {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Variant value cannot be null or empty");
        }
        value = value.trim();
    }
}