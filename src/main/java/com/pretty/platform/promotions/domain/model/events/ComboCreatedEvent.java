package com.pretty.platform.promotions.domain.model.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Event triggered when a combo is created
 */
public record ComboCreatedEvent(
    UUID comboId,
    String comboName,
    List<UUID> productIds,
    BigDecimal comboPrice,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
}