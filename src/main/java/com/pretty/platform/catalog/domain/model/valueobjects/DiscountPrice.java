package com.pretty.platform.catalog.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Value object for product discount price
 */
@Embeddable
public record DiscountPrice(@Column(name = "discount_price_amount") BigDecimal amount) {
    public DiscountPrice {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount price must be positive");
        }
    }
}