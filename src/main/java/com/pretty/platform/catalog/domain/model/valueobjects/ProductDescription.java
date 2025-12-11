package com.pretty.platform.catalog.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object for product description
 */
@Embeddable
public record ProductDescription(String description) {
    public ProductDescription {
        if (description == null) {
            description = "";
        }
    }
}