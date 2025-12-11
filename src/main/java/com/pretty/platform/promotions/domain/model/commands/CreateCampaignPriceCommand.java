package com.pretty.platform.promotions.domain.model.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Command to create campaign price for a product
 */
public record CreateCampaignPriceCommand(
    UUID productId,
    String campaignName,
    BigDecimal campaignPrice,
    String currency,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
    public CreateCampaignPriceCommand {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (campaignName == null || campaignName.trim().isEmpty()) {
            throw new IllegalArgumentException("Campaign name cannot be null or empty");
        }
        if (campaignPrice == null || campaignPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Campaign price must be positive");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }
}