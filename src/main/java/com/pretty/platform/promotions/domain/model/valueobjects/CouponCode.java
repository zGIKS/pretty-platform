package com.pretty.platform.promotions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing a coupon code
 */
@Embeddable
public record CouponCode(String code) {
    public CouponCode {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Coupon code cannot be null or empty");
        }
        code = code.trim().toUpperCase();
        if (code.length() < 3 || code.length() > 20) {
            throw new IllegalArgumentException("Coupon code must be between 3 and 20 characters");
        }
    }
}