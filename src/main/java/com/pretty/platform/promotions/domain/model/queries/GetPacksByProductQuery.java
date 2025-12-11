package com.pretty.platform.promotions.domain.model.queries;

import java.util.UUID;

/**
 * Query to get all packs that include a specific product
 */
public record GetPacksByProductQuery(UUID productId) {
}