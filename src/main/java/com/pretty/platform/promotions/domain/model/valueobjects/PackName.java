package com.pretty.platform.promotions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing a pack name
 */
@Embeddable
public record PackName(String name) {
    public PackName {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Pack name cannot be null or empty");
        }
        name = name.trim();
    }
}