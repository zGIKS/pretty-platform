package com.pretty.platform.promotions.domain.model.queries;

import java.util.UUID;

/**
 * Query to get all combos that include a specific product
 */
public record GetCombosByProductQuery(UUID productId) {
}