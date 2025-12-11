package com.pretty.platform.promotions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Value object representing a discount percentage (0-100)
 */
@Embeddable
public record DiscountPercentage(int percentage) {
    public DiscountPercentage {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
    }

    public Price applyTo(Price originalPrice) {
        var discountAmount = originalPrice.amount().multiply(BigDecimal.valueOf(percentage).divide(BigDecimal.valueOf(100)));
        return new Price(originalPrice.amount().subtract(discountAmount), originalPrice.currency());
    }
}