package com.pretty.platform.promotions.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Resource for creating campaign prices
 */
public record CreateCampaignPriceResource(
    String campaignName,
    String productId,
    BigDecimal campaignPrice,
    String currency,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
}