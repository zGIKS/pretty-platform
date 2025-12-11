package com.pretty.platform.promotions.domain.model.queries;

import java.util.UUID;

/**
 * Query to get all campaigns applicable to a specific product
 */
public record GetCampaignsByProductQuery(UUID productId) {
}