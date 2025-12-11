package com.pretty.platform.promotions.domain.model.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Event triggered when a pack is created
 */
public record PackCreatedEvent(
    UUID packId,
    String packName,
    List<UUID> productIds,
    BigDecimal packPrice,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
}