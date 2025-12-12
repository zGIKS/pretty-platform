package com.pretty.platform.inventory.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing low stock threshold
 */
@Embeddable
public record Threshold(int value) {
    public Threshold {
        if (value < 0) {
            throw new IllegalArgumentException("Threshold cannot be negative");
        }
    }
}