package com.pretty.platform.promotions.domain.model.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event triggered when a campaign price is created
 */
public record CampaignPriceCreatedEvent(
    UUID campaignId,
    String campaignName,
    UUID productId,
    BigDecimal campaignPrice,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
}