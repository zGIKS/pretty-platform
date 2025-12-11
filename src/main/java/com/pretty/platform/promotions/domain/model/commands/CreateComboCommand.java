package com.pretty.platform.promotions.domain.model.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Command to create a combo (products with discount when bought together)
 */
public record CreateComboCommand(
    String comboName,
    List<UUID> productIds,
    BigDecimal comboPrice,
    String currency,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
    public CreateComboCommand {
        if (comboName == null || comboName.trim().isEmpty()) {
            throw new IllegalArgumentException("Combo name cannot be null or empty");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs cannot be null or empty");
        }
        if (comboPrice == null || comboPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Combo price must be positive");
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