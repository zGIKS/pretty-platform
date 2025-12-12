package com.pretty.platform.shared.domain.model.aggregates;

import com.pretty.platform.promotions.domain.model.events.DiscountCreatedEvent;
import com.pretty.platform.promotions.domain.model.valueobjects.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Discount aggregate root
 */
@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "product_id"))
    })
    private ProductId productId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "discount_amount"))
    })
    private DiscountAmount discountAmount;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "percentage", column = @Column(name = "discount_percentage"))
    })
    private DiscountPercentage discountPercentage;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startDate", column = @Column(name = "start_date")),
        @AttributeOverride(name = "endDate", column = @Column(name = "end_date"))
    })
    private ValidityPeriod validityPeriod;

    @Version
    private Long version;

    // No-args constructor for JPA
    protected Discount() {}

    public Discount(ProductId productId, DiscountAmount discountAmount,
                   DiscountPercentage discountPercentage, ValidityPeriod validityPeriod) {
        this.productId = productId;
        this.discountAmount = discountAmount;
        this.discountPercentage = discountPercentage;
        this.validityPeriod = validityPeriod;

        // Publish domain event
        addDomainEvent(new DiscountCreatedEvent(
            this.id,
            productId.getId(),
            discountAmount != null ? discountAmount.amount() : null,
            discountPercentage != null ? BigDecimal.valueOf(discountPercentage.percentage()) : null,
            validityPeriod.startDate(),
            validityPeriod.endDate()
        ));
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(validityPeriod.startDate()) && now.isBefore(validityPeriod.endDate());
    }

    public BigDecimal calculateDiscountedPrice(BigDecimal basePrice) {
        BigDecimal discountedPrice = basePrice;

        if (discountAmount != null && discountAmount.amount().compareTo(BigDecimal.ZERO) > 0) {
            discountedPrice = discountedPrice.subtract(discountAmount.amount());
        }

        if (discountPercentage != null && discountPercentage.percentage() > 0) {
            BigDecimal percentageDiscount = discountedPrice.multiply(BigDecimal.valueOf(discountPercentage.percentage()).divide(BigDecimal.valueOf(100)));
            discountedPrice = discountedPrice.subtract(percentageDiscount);
        }

        return discountedPrice.max(BigDecimal.ZERO);
    }

    public UUID getId() {
        return id;
    }

    public ProductId getProductId() {
        return productId;
    }

    public DiscountAmount getDiscountAmount() {
        return discountAmount;
    }

    public DiscountPercentage getDiscountPercentage() {
        return discountPercentage;
    }

    public ValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public Long getVersion() {
        return version;
    }

    private void addDomainEvent(DiscountCreatedEvent event) {
        // Domain event publishing logic would go here
        // For now, we'll just create the event
    }
}