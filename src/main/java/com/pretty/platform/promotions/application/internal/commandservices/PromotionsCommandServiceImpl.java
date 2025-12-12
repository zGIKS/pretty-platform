package com.pretty.platform.promotions.application.internal.commandservices;

import com.pretty.platform.promotions.application.internal.outboundservices.acl.ExternalProductService;
import com.pretty.platform.promotions.domain.model.commands.*;
import com.pretty.platform.promotions.domain.services.PromotionsCommandService;
import com.pretty.platform.promotions.infrastructure.persistence.jpa.repositories.*;
import com.pretty.platform.shared.domain.model.aggregates.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PromotionsCommandService
 */
@Service
public class PromotionsCommandServiceImpl implements PromotionsCommandService {

    private final BasePriceRepository basePriceRepository;
    private final DiscountRepository discountRepository;
    private final ExternalProductService externalProductService;

    public PromotionsCommandServiceImpl(
            BasePriceRepository basePriceRepository,
            DiscountRepository discountRepository,
            ExternalProductService externalProductService) {
        this.basePriceRepository = basePriceRepository;
        this.discountRepository = discountRepository;
        this.externalProductService = externalProductService;
    }

    @Override
    @Transactional
    public void createBasePrice(CreateBasePriceCommand command) {
        // Validate product exists
        externalProductService.validateProductExists(command.productId());

        var productId = new com.pretty.platform.promotions.domain.model.valueobjects.ProductId(command.productId());
        var price = new com.pretty.platform.promotions.domain.model.valueobjects.Price(command.price(), command.currency());

        var basePrice = new BasePrice(productId, price);
        basePriceRepository.save(basePrice);
    }

    @Override
    @Transactional
    public void createDiscount(CreateDiscountCommand command) {
        // Validate product exists
        externalProductService.validateProductExists(command.productId());

        var productId = new com.pretty.platform.promotions.domain.model.valueobjects.ProductId(command.productId());
        var discountAmount = command.discountAmount() != null ?
            new com.pretty.platform.promotions.domain.model.valueobjects.DiscountAmount(command.discountAmount()) : null;
        var discountPercentage = command.discountPercentage() != null ?
            new com.pretty.platform.promotions.domain.model.valueobjects.DiscountPercentage(command.discountPercentage()) : null;
        var validityPeriod = new com.pretty.platform.promotions.domain.model.valueobjects.ValidityPeriod(command.startDate(), command.endDate());

        var discount = new Discount(productId, discountAmount, discountPercentage, validityPeriod);
        discountRepository.save(discount);
    }
}