package com.pretty.platform.catalog.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object for product image URL
 */
@Embeddable
public record ImageUrl(String url) {
    public ImageUrl {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be null or empty");
        }
    }
}