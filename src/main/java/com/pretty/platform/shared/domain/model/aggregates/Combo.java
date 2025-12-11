package com.pretty.platform.shared.domain.model.aggregates;

import com.pretty.platform.promotions.domain.model.events.ComboCreatedEvent;
import com.pretty.platform.promotions.domain.model.valueobjects.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Combo aggregate root
 */
@Entity
@Table(name = "combos")
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "combo_name"))
    })
    private ComboName comboName;

    @ElementCollection
    @CollectionTable(name = "combo_products", joinColumns = @JoinColumn(name = "combo_id"))
    @Column(name = "product_id")
    private List<UUID> productIds;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "combo_price_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "combo_price_currency"))
    })
    private Price comboPrice;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startDate", column = @Column(name = "start_date")),
        @AttributeOverride(name = "endDate", column = @Column(name = "end_date"))
    })
    private ValidityPeriod validityPeriod;

    @Version
    private Long version;

    // No-args constructor for JPA
    protected Combo() {}

    public Combo(ComboName comboName, List<UUID> productIds, Price comboPrice, ValidityPeriod validityPeriod) {
        this.comboName = comboName;
        this.productIds = productIds;
        this.comboPrice = comboPrice;
        this.validityPeriod = validityPeriod;

        // Publish domain event
        addDomainEvent(new ComboCreatedEvent(
            this.id,
            comboName.name(),
            productIds,
            comboPrice.amount(),
            validityPeriod.startDate(),
            validityPeriod.endDate()
        ));
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(validityPeriod.startDate()) && now.isBefore(validityPeriod.endDate());
    }

    public BigDecimal getComboAmount() {
        return comboPrice.amount();
    }

    public String getComboCurrency() {
        return comboPrice.currency();
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

    public ComboName getComboName() {
        return comboName;
    }

    public List<UUID> getProductIds() {
        return productIds;
    }

    public Price getComboPrice() {
        return comboPrice;
    }

    public ValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public Long getVersion() {
        return version;
    }

    private void addDomainEvent(ComboCreatedEvent event) {
        // Domain event publishing logic would go here
        // For now, we'll just create the event
    }
}