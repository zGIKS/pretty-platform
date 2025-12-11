package com.pretty.platform.promotions.domain.model.queries;

import java.util.UUID;

/**
 * Query to get all coupons applicable to a specific product
 */
public record GetCouponsByProductQuery(UUID productId) {
}