package com.pretty.platform.shared.domain.model.aggregates;

import com.pretty.platform.promotions.domain.model.events.CampaignPriceCreatedEvent;
import com.pretty.platform.promotions.domain.model.valueobjects.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CampaignPrice aggregate root
 */
@Entity
@Table(name = "campaign_prices")
public class CampaignPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "campaign_name"))
    })
    private CampaignName campaignName;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "product_id"))
    })
    private ProductId productId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "campaign_price_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "campaign_price_currency"))
    })
    private Price campaignPrice;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startDate", column = @Column(name = "start_date")),
        @AttributeOverride(name = "endDate", column = @Column(name = "end_date"))
    })
    private ValidityPeriod validityPeriod;

    @Version
    private Long version;

    // No-args constructor for JPA
    protected CampaignPrice() {}

    public CampaignPrice(CampaignName campaignName, ProductId productId,
                        Price campaignPrice, ValidityPeriod validityPeriod) {
        this.campaignName = campaignName;
        this.productId = productId;
        this.campaignPrice = campaignPrice;
        this.validityPeriod = validityPeriod;

        // Publish domain event
        addDomainEvent(new CampaignPriceCreatedEvent(
            this.id,
            campaignName.name(),
            productId.getId(),
            campaignPrice.amount(),
            validityPeriod.startDate(),
            validityPeriod.endDate()
        ));
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(validityPeriod.startDate()) && now.isBefore(validityPeriod.endDate());
    }

    public BigDecimal getCampaignAmount() {
        return campaignPrice.amount();
    }

    public String getCampaignCurrency() {
        return campaignPrice.currency();
    }

    public UUID getId() {
        return id;
    }

    public CampaignName getCampaignName() {
        return campaignName;
    }

    public ProductId getProductId() {
        return productId;
    }

    public Price getCampaignPrice() {
        return campaignPrice;
    }

    public ValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public Long getVersion() {
        return version;
    }

    private void addDomainEvent(CampaignPriceCreatedEvent event) {
        // Domain event publishing logic would go here
        // For now, we'll just create the event
    }
}