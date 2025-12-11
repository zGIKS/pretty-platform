package com.pretty.platform.promotions.domain.model.queries;

import java.util.UUID;

/**
 * Query to get a pack by its ID
 */
public record GetPackByIdQuery(UUID packId) {
}