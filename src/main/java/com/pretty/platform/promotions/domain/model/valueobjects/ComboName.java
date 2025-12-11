package com.pretty.platform.promotions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing a combo name
 */
@Embeddable
public record ComboName(String name) {
    public ComboName {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Combo name cannot be null or empty");
        }
        name = name.trim();
    }
}