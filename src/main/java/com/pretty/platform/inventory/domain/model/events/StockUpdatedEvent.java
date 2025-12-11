package com.pretty.platform.inventory.domain.model.events;

import com.pretty.platform.inventory.domain.model.valueobjects.ProductId;
import com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity;
import com.pretty.platform.inventory.domain.model.valueobjects.Variant;

import java.time.LocalDateTime;

/**
 * Event fired when stock is updated
 */
public class StockUpdatedEvent {
    private final Long inventoryId;
    private final ProductId productId;
    private final Variant variant;
    private final StockQuantity availableStock;
    private final StockQuantity reservedStock;
    private final StockQuantity soldStock;
    private final LocalDateTime occurredOn;

    public StockUpdatedEvent(Long inventoryId, ProductId productId, Variant variant,
                           StockQuantity availableStock, StockQuantity reservedStock, StockQuantity soldStock) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.variant = variant;
        this.availableStock = availableStock;
        this.reservedStock = reservedStock;
        this.soldStock = soldStock;
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

    public StockQuantity getAvailableStock() {
        return availableStock;
    }

    public StockQuantity getReservedStock() {
        return reservedStock;
    }

    public StockQuantity getSoldStock() {
        return soldStock;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}