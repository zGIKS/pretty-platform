package com.pretty.platform.promotions.domain.model.queries;

/**
 * Query to get coupon by code
 */
public record GetCouponByCodeQuery(String couponCode) {
    public GetCouponByCodeQuery {
        if (couponCode == null || couponCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Coupon code cannot be null or empty");
        }
    }
}