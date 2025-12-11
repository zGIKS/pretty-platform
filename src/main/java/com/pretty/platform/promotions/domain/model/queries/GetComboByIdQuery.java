package com.pretty.platform.promotions.domain.model.queries;

import java.util.UUID;

/**
 * Query to get a combo by its ID
 */
public record GetComboByIdQuery(UUID comboId) {
}