package com.pretty.platform.inventory.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing stock quantity
 */
@Embeddable
public record StockQuantity(int value) {
    public StockQuantity {
        if (value < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
    }

    public StockQuantity add(StockQuantity other) {
        return new StockQuantity(this.value + other.value);
    }

    public StockQuantity subtract(StockQuantity other) {
        return new StockQuantity(this.value - other.value);
    }

    public boolean isGreaterThan(StockQuantity other) {
        return this.value > other.value;
    }

    public boolean isZero() {
        return this.value == 0;
    }
}