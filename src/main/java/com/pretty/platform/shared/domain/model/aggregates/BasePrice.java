package com.pretty.platform.shared.domain.model.aggregates;

import com.pretty.platform.promotions.domain.model.events.BasePriceCreatedEvent;
import com.pretty.platform.promotions.domain.model.valueobjects.Price;
import com.pretty.platform.promotions.domain.model.valueobjects.ProductId;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * BasePrice aggregate root
 */
@Entity
@Table(name = "base_prices")
public class BasePrice {

    @EmbeddedId
    private ProductId productId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Price price;

    @Version
    private Long version;

    // No-args constructor for JPA
    protected BasePrice() {}

    public BasePrice(ProductId productId, Price price) {
        this.productId = productId;
        this.price = price;

        // Publish domain event
        addDomainEvent(new BasePriceCreatedEvent(productId.getId(), price.amount()));
    }

    public void updatePrice(Price newPrice) {
        this.price = newPrice;
    }

    public ProductId getProductId() {
        return productId;
    }

    public Price getPrice() {
        return price;
    }

    public Long getVersion() {
        return version;
    }

    public BigDecimal getAmount() {
        return price.amount();
    }

    public String getCurrency() {
        return price.currency();
    }

    private void addDomainEvent(BasePriceCreatedEvent event) {
        // Domain event publishing logic would go here
        // For now, we'll just create the event
    }
}