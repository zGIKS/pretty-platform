package com.pretty.platform.promotions.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Resource for creating combos
 */
public record CreateComboResource(
    String comboName,
    List<String> productIds,
    BigDecimal comboPrice,
    String currency,
    LocalDateTime startDate,
    LocalDateTime endDate
) {
}