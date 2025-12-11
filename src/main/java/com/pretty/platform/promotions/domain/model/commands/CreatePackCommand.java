package com.pretty.platform.promotions.domain.model.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Command to create a pack (bundle of products)
 */
public record CreatePackCommand(
    String packName,
    List<UUID> productIds,
    BigDecimal packPrice,
    String currency,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
    public CreatePackCommand {
        if (packName == null || packName.trim().isEmpty()) {
            throw new IllegalArgumentException("Pack name cannot be null or empty");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs cannot be null or empty");
        }
        if (packPrice == null || packPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Pack price must be positive");
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