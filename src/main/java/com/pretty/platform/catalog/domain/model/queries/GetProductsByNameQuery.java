package com.pretty.platform.catalog.domain.model.queries;

/**
 * Query to get products by name search
 */
public record GetProductsByNameQuery(String searchTerm) {
    public GetProductsByNameQuery {
        if (searchTerm == null || searchTerm.isBlank()) {
            throw new IllegalArgumentException("Search term cannot be null or empty");
        }
    }
}
