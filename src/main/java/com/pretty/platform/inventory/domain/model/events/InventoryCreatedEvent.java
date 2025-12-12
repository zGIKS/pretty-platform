package com.pretty.platform.inventory.domain.model.events;

import com.pretty.platform.inventory.domain.model.valueobjects.ProductId;
import com.pretty.platform.inventory.domain.model.valueobjects.Variant;

import java.time.LocalDateTime;

/**
 * Event fired when inventory is created
 */
public class InventoryCreatedEvent {
    private final Long inventoryId;
    private final ProductId productId;
    private final Variant variant;
    private final LocalDateTime occurredOn;

    public InventoryCreatedEvent(Long inventoryId, ProductId productId, Variant variant) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.variant = variant;
        this.occurredOn = LocalDateTime.now();
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public Variant getVariant() {
        return variant;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}