package com.pretty.platform.promotions.domain.model.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event triggered when a coupon is created
 */
public record CouponCreatedEvent(
    UUID couponId,
    String couponCode,
    UUID productId,
    BigDecimal discountAmount,
    BigDecimal discountPercentage,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
}