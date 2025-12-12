package com.pretty.platform.promotions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Value object representing a discount amount
 */
@Embeddable
public record DiscountAmount(BigDecimal amount) {
    public DiscountAmount {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount amount must be non-negative");
        }
    }
}