package com.pretty.platform.promotions.interfaces.rest.resources;

import java.math.BigDecimal;

/**
 * Resource for creating base prices
 */
public record CreateBasePriceResource(
    String productId,
    BigDecimal price,
    String currency
) {
}