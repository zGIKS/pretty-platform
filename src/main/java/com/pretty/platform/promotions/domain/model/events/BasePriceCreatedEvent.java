package com.pretty.platform.promotions.domain.model.events;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Event triggered when a base price is created
 */
public record BasePriceCreatedEvent(
    UUID productId,
    BigDecimal price
) {
}