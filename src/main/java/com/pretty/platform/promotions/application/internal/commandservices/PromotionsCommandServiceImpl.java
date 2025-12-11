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
    private final CampaignPriceRepository campaignPriceRepository;
    private final CouponRepository couponRepository;
    private final PackRepository packRepository;
    private final ComboRepository comboRepository;
    private final ExternalProductService externalProductService;

    public PromotionsCommandServiceImpl(
            BasePriceRepository basePriceRepository,
            DiscountRepository discountRepository,
            CampaignPriceRepository campaignPriceRepository,
            CouponRepository couponRepository,
            PackRepository packRepository,
            ComboRepository comboRepository,
            ExternalProductService externalProductService) {
        this.basePriceRepository = basePriceRepository;
        this.discountRepository = discountRepository;
        this.campaignPriceRepository = campaignPriceRepository;
        this.couponRepository = couponRepository;
        this.packRepository = packRepository;
        this.comboRepository = comboRepository;
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

    @Override
    @Transactional
    public void createCampaignPrice(CreateCampaignPriceCommand command) {
        // Validate product exists
        externalProductService.validateProductExists(command.productId());

        var campaignName = new com.pretty.platform.promotions.domain.model.valueobjects.CampaignName(command.campaignName());
        var productId = new com.pretty.platform.promotions.domain.model.valueobjects.ProductId(command.productId());
        var campaignPrice = new com.pretty.platform.promotions.domain.model.valueobjects.Price(command.campaignPrice(), command.currency());
        var validityPeriod = new com.pretty.platform.promotions.domain.model.valueobjects.ValidityPeriod(command.startDate(), command.endDate());

        var campaignPriceEntity = new CampaignPrice(campaignName, productId, campaignPrice, validityPeriod);
        campaignPriceRepository.save(campaignPriceEntity);
    }

    @Override
    @Transactional
    public void createCoupon(CreateCouponCommand command) {
        // Validate product exists
        externalProductService.validateProductExists(command.productId());

        var couponCode = new com.pretty.platform.promotions.domain.model.valueobjects.CouponCode(command.couponCode());
        var productId = new com.pretty.platform.promotions.domain.model.valueobjects.ProductId(command.productId());
        var discountAmount = command.discountAmount() != null ?
            new com.pretty.platform.promotions.domain.model.valueobjects.DiscountAmount(command.discountAmount()) : null;
        var discountPercentage = command.discountPercentage() != null ?
            new com.pretty.platform.promotions.domain.model.valueobjects.DiscountPercentage(command.discountPercentage()) : null;
        var validityPeriod = new com.pretty.platform.promotions.domain.model.valueobjects.ValidityPeriod(command.startDate(), command.endDate());

        var coupon = new Coupon(couponCode, productId, discountAmount, discountPercentage, validityPeriod, command.maxUsage());
        couponRepository.save(coupon);
    }

    @Override
    @Transactional
    public void createPack(CreatePackCommand command) {
        // Validate all products exist
        for (var productId : command.productIds()) {
            externalProductService.validateProductExists(productId);
        }

        var packName = new com.pretty.platform.promotions.domain.model.valueobjects.PackName(command.packName());
        var packPrice = new com.pretty.platform.promotions.domain.model.valueobjects.Price(command.packPrice(), command.currency());
        var validityPeriod = new com.pretty.platform.promotions.domain.model.valueobjects.ValidityPeriod(command.startDate(), command.endDate());

        var pack = new Pack(packName, command.productIds(), packPrice, validityPeriod);
        packRepository.save(pack);
    }

    @Override
    @Transactional
    public void createCombo(CreateComboCommand command) {
        // Validate all products exist
        for (var productId : command.productIds()) {
            externalProductService.validateProductExists(productId);
        }

        var comboName = new com.pretty.platform.promotions.domain.model.valueobjects.ComboName(command.comboName());
        var comboPrice = new com.pretty.platform.promotions.domain.model.valueobjects.Price(command.comboPrice(), command.currency());
        var validityPeriod = new com.pretty.platform.promotions.domain.model.valueobjects.ValidityPeriod(command.startDate(), command.endDate());

        var combo = new Combo(comboName, command.productIds(), comboPrice, validityPeriod);
        comboRepository.save(combo);
    }
}