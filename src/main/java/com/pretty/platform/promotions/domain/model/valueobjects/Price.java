package com.pretty.platform.promotions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Value object representing a price amount
 */
@Embeddable
public record Price(BigDecimal amount, String currency) {
    public Price {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
    }

    public Price add(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add prices with different currencies");
        }
        return new Price(this.amount.add(other.amount), this.currency);
    }

    public Price subtract(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract prices with different currencies");
        }
        return new Price(this.amount.subtract(other.amount), this.currency);
    }

    public Price multiply(BigDecimal factor) {
        return new Price(this.amount.multiply(factor), this.currency);
    }

    public boolean isGreaterThan(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare prices with different currencies");
        }
        return this.amount.compareTo(other.amount) > 0;
    }
}