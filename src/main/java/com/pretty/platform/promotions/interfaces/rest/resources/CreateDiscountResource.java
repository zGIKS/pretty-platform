package com.pretty.platform.promotions.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Resource for creating discounts
 */
public record CreateDiscountResource(
    String productId,
    BigDecimal discountAmount,
    BigDecimal discountPercentage,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
}