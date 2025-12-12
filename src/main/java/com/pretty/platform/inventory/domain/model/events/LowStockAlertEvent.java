package com.pretty.platform.inventory.domain.model.events;

import com.pretty.platform.inventory.domain.model.valueobjects.ProductId;
import com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity;
import com.pretty.platform.inventory.domain.model.valueobjects.Threshold;
import com.pretty.platform.inventory.domain.model.valueobjects.Variant;

import java.time.LocalDateTime;

/**
 * Event fired when stock falls below threshold
 */
public class LowStockAlertEvent {
    private final Long inventoryId;
    private final ProductId productId;
    private final Variant variant;
    private final StockQuantity currentStock;
    private final Threshold threshold;
    private final LocalDateTime occurredOn;

    public LowStockAlertEvent(Long inventoryId, ProductId productId, Variant variant,
                            StockQuantity currentStock, Threshold threshold) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.variant = variant;
        this.currentStock = currentStock;
        this.threshold = threshold;
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

    public StockQuantity getCurrentStock() {
        return currentStock;
    }

    public Threshold getThreshold() {
        return threshold;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}