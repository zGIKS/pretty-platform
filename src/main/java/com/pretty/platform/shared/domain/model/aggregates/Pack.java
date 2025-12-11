package com.pretty.platform.shared.domain.model.aggregates;

import com.pretty.platform.promotions.domain.model.events.PackCreatedEvent;
import com.pretty.platform.promotions.domain.model.valueobjects.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Pack aggregate root
 */
@Entity
@Table(name = "packs")
public class Pack {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "pack_name"))
    })
    private PackName packName;

    @ElementCollection
    @CollectionTable(name = "pack_products", joinColumns = @JoinColumn(name = "pack_id"))
    @Column(name = "product_id")
    private List<UUID> productIds;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "pack_price_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "pack_price_currency"))
    })
    private Price packPrice;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startDate", column = @Column(name = "start_date")),
        @AttributeOverride(name = "endDate", column = @Column(name = "end_date"))
    })
    private ValidityPeriod validityPeriod;

    @Version
    private Long version;

    // No-args constructor for JPA
    protected Pack() {}

    public Pack(PackName packName, List<UUID> productIds, Price packPrice, ValidityPeriod validityPeriod) {
        this.packName = packName;
        this.productIds = productIds;
        this.packPrice = packPrice;
        this.validityPeriod = validityPeriod;

        // Publish domain event
        addDomainEvent(new PackCreatedEvent(
            this.id,
            packName.name(),
            productIds,
            packPrice.amount(),
            validityPeriod.startDate(),
            validityPeriod.endDate()
        ));
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(validityPeriod.startDate()) && now.isBefore(validityPeriod.endDate());
    }

    public BigDecimal getPackAmount() {
        return packPrice.amount();
    }

    public String getPackCurrency() {
        return packPrice.currency();
    }

    public boolean containsProduct(UUID productId) {
        return productIds.contains(productId);
    }

    public int getProductCount() {
        return productIds.size();
    }

    public UUID getId() {
        return id;
    }

    public PackName getPackName() {
        return packName;
    }

    public List<UUID> getProductIds() {
        return productIds;
    }

    public Price getPackPrice() {
        return packPrice;
    }

    public ValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public Long getVersion() {
        return version;
    }

    private void addDomainEvent(PackCreatedEvent event) {
        // Domain event publishing logic would go here
        // For now, we'll just create the event
    }
}