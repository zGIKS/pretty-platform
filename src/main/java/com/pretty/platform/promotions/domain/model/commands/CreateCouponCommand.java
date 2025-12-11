package com.pretty.platform.promotions.domain.model.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Command to create a coupon
 */
public record CreateCouponCommand(
    String couponCode,
    UUID productId,
    BigDecimal discountAmount,
    Integer discountPercentage,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Integer maxUsage
) {
    public CreateCouponCommand {
        if (couponCode == null || couponCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Coupon code cannot be null or empty");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (discountAmount == null && discountPercentage == null) {
            throw new IllegalArgumentException("Either discount amount or percentage must be provided");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }
        if (discountPercentage != null && (discountPercentage < 0 || discountPercentage > 100)) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        if (maxUsage != null && maxUsage <= 0) {
            throw new IllegalArgumentException("Max usage must be positive");
        }
    }
}