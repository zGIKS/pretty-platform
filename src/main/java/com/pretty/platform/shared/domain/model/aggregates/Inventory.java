package com.pretty.platform.shared.domain.model.aggregates;

import com.pretty.platform.inventory.domain.model.events.LowStockAlertEvent;
import com.pretty.platform.inventory.domain.model.events.StockUpdatedEvent;
import com.pretty.platform.inventory.domain.model.valueobjects.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Inventory aggregate root
 */
@Entity
@Table(name = "inventories")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ProductId productId;

    @Embedded
    private Variant variant;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "available_stock"))
    })
    private StockQuantity availableStock;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "reserved_stock"))
    })
    private StockQuantity reservedStock;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "sold_stock"))
    })
    private StockQuantity soldStock;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "low_stock_threshold"))
    })
    private Threshold lowStockThreshold;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

    protected Inventory() {}

    public Inventory(ProductId productId, Variant variant, StockQuantity initialStock, Threshold lowStockThreshold) {
        this.productId = productId;
        this.variant = variant;
        this.availableStock = initialStock;
        this.reservedStock = new StockQuantity(0);
        this.soldStock = new StockQuantity(0);
        this.lowStockThreshold = lowStockThreshold;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public void updateStock(StockQuantity available, StockQuantity reserved, StockQuantity sold) {
        this.availableStock = available;
        this.reservedStock = reserved;
        this.soldStock = sold;
        this.updatedAt = LocalDateTime.now();

        domainEvents.add(new StockUpdatedEvent(this.id, this.productId, this.variant,
                                             this.availableStock, this.reservedStock, this.soldStock));

        if (this.availableStock.value() <= this.lowStockThreshold.value()) {
            domainEvents.add(new LowStockAlertEvent(this.id, this.productId, this.variant,
                                                  this.availableStock, this.lowStockThreshold));
        }
    }

    public boolean canReserve(StockQuantity quantity) {
        return this.availableStock.isGreaterThan(quantity) || this.availableStock.value() == quantity.value();
    }

    public void reserveStock(StockQuantity quantity) {
        if (!canReserve(quantity)) {
            throw new IllegalStateException("Insufficient available stock");
        }
        this.availableStock = this.availableStock.subtract(quantity);
        this.reservedStock = this.reservedStock.add(quantity);
        this.updatedAt = LocalDateTime.now();
    }

    public void releaseStock(StockQuantity quantity) {
        if (this.reservedStock.value() < quantity.value()) {
            throw new IllegalStateException("Cannot release more than reserved stock");
        }
        this.reservedStock = this.reservedStock.subtract(quantity);
        this.availableStock = this.availableStock.add(quantity);
        this.updatedAt = LocalDateTime.now();
    }

    public void sellStock(StockQuantity quantity) {
        if (this.reservedStock.value() < quantity.value()) {
            throw new IllegalStateException("Cannot sell more than reserved stock");
        }
        this.reservedStock = this.reservedStock.subtract(quantity);
        this.soldStock = this.soldStock.add(quantity);
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isLowStock() {
        return this.availableStock.value() <= this.lowStockThreshold.value();
    }

    public StockQuantity getTotalStock() {
        return new StockQuantity(this.availableStock.value() + this.reservedStock.value() + this.soldStock.value());
    }

    // Getters
    public Long getId() { return id; }
    public ProductId getProductId() { return productId; }
    public Variant getVariant() { return variant; }
    public StockQuantity getAvailableStock() { return availableStock; }
    public StockQuantity getReservedStock() { return reservedStock; }
    public StockQuantity getSoldStock() { return soldStock; }
    public Threshold getLowStockThreshold() { return lowStockThreshold; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<Object> getDomainEvents() { return new ArrayList<>(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }
}