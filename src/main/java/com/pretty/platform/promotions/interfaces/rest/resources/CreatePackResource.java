package com.pretty.platform.promotions.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Resource for creating packs
 */
public record CreatePackResource(
    String packName,
    List<String> productIds,
    BigDecimal packPrice,
    String currency,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
}