package com.pretty.platform.promotions.domain.model.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event triggered when a discount is created
 */
public record DiscountCreatedEvent(
    UUID discountId,
    UUID productId,
    BigDecimal discountAmount,
    BigDecimal discountPercentage,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
}