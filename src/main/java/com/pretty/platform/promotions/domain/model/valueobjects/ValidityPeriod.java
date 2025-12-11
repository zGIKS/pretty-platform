package com.pretty.platform.promotions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * Value object representing a validity period
 */
@Embeddable
public record ValidityPeriod(LocalDateTime startDate, LocalDateTime endDate) {
    public ValidityPeriod {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }

    public boolean isActive() {
        var now = LocalDateTime.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(endDate);
    }
}