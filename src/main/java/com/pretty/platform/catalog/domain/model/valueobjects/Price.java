package com.pretty.platform.catalog.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Price value object
 */
@Embeddable
public record Price(BigDecimal amount, String currency) {

    public Price {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price amount must be non-negative");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
    }

    public Price(BigDecimal amount) {
        this(amount, "USD");
    }
}