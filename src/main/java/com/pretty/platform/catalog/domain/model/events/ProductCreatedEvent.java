package com.pretty.platform.catalog.domain.model.events;

import java.time.LocalDateTime;

/**
 * Event raised when a product is created
 */
public class ProductCreatedEvent {
    private final Long productId;
    private final LocalDateTime occurredOn;

    public ProductCreatedEvent(Long productId) {
        this.productId = productId;
        this.occurredOn = LocalDateTime.now();
    }

    public Long getProductId() { return productId; }
    public LocalDateTime getOccurredOn() { return occurredOn; }
}