package com.pretty.platform.promotions.domain.model.queries;

import java.util.UUID;

/**
 * Query to get all discounts for a specific product
 */
public record GetDiscountsByProductQuery(UUID productId) {
}