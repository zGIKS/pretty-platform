package com.pretty.platform.catalog.domain.model.queries;

/**
 * Query to get products filtered by category and/or tag
 */
public record GetProductsByCategoryAndTagQuery(String category, String tag) {
    public GetProductsByCategoryAndTagQuery {
        if (category == null && tag == null) {
            throw new IllegalArgumentException("At least one filter parameter (category or tag) must be provided");
        }
    }
}
