package com.pretty.platform.promotions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing a campaign name
 */
@Embeddable
public record CampaignName(String name) {
    public CampaignName {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Campaign name cannot be null or empty");
        }
        name = name.trim();
    }
}