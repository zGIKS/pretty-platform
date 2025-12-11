package com.pretty.platform.promotions.application.internal.queryservices;

import com.pretty.platform.promotions.domain.model.queries.*;
import com.pretty.platform.promotions.domain.services.PromotionsQueryService;
import com.pretty.platform.promotions.infrastructure.persistence.jpa.repositories.*;
import com.pretty.platform.shared.domain.model.aggregates.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of PromotionsQueryService
 */
@Service
@Transactional(readOnly = true)
public class PromotionsQueryServiceImpl implements PromotionsQueryService {

    private final BasePriceRepository basePriceRepository;
    private final DiscountRepository discountRepository;
    private final CampaignPriceRepository campaignPriceRepository;
    private final CouponRepository couponRepository;
    private final PackRepository packRepository;
    private final ComboRepository comboRepository;

    public PromotionsQueryServiceImpl(
            BasePriceRepository basePriceRepository,
            DiscountRepository discountRepository,
            CampaignPriceRepository campaignPriceRepository,
            CouponRepository couponRepository,
            PackRepository packRepository,
            ComboRepository comboRepository) {
        this.basePriceRepository = basePriceRepository;
        this.discountRepository = discountRepository;
        this.campaignPriceRepository = campaignPriceRepository;
        this.couponRepository = couponRepository;
        this.packRepository = packRepository;
        this.comboRepository = comboRepository;
    }

    @Override
    public Optional<BasePrice> getBasePrice(GetBasePriceQuery query) {
        var productId = new com.pretty.platform.promotions.domain.model.valueobjects.ProductId(query.productId());
        return basePriceRepository.findByProductId(productId);
    }

    @Override
    public List<Discount> getActiveDiscounts(GetActiveDiscountsQuery query) {
        return discountRepository.findAllActiveDiscounts(LocalDateTime.now());
    }

    @Override
    public List<CampaignPrice> getActiveCampaigns(GetActiveCampaignsQuery query) {
        return campaignPriceRepository.findAllActiveCampaigns(LocalDateTime.now());
    }

    @Override
    public Optional<Coupon> getCouponByCode(GetCouponByCodeQuery query) {
        var couponCode = new com.pretty.platform.promotions.domain.model.valueobjects.CouponCode(query.couponCode());
        return couponRepository.findByCouponCode(couponCode);
    }

    @Override
    public List<Pack> getActivePacks(GetActivePacksQuery query) {
        return packRepository.findAllActivePacks(LocalDateTime.now());
    }

    @Override
    public List<Combo> getActiveCombos(GetActiveCombosQuery query) {
        return comboRepository.findAllActiveCombos(LocalDateTime.now());
    }

    @Override
    public Optional<Pack> getPackById(GetPackByIdQuery query) {
        return packRepository.findById(query.packId());
    }

    @Override
    public Optional<Combo> getComboById(GetComboByIdQuery query) {
        return comboRepository.findById(query.comboId());
    }

    @Override
    public List<Pack> getPacksByProduct(GetPacksByProductQuery query) {
        return packRepository.findByProductId(query.productId());
    }

    @Override
    public List<Combo> getCombosByProduct(GetCombosByProductQuery query) {
        return comboRepository.findByProductId(query.productId());
    }

    @Override
    public List<Discount> getDiscountsByProduct(GetDiscountsByProductQuery query) {
        var productId = new com.pretty.platform.promotions.domain.model.valueobjects.ProductId(query.productId());
        return discountRepository.findByProductId(productId);
    }

    @Override
    public List<Coupon> getCouponsByProduct(GetCouponsByProductQuery query) {
        var productId = new com.pretty.platform.promotions.domain.model.valueobjects.ProductId(query.productId());
        return couponRepository.findByProductId(productId);
    }

    @Override
    public List<CampaignPrice> getCampaignsByProduct(GetCampaignsByProductQuery query) {
        var productId = new com.pretty.platform.promotions.domain.model.valueobjects.ProductId(query.productId());
        return campaignPriceRepository.findByProductId(productId);
    }

    @Override
    public List<Pack> getAllPacks(GetAllPacksQuery query) {
        return packRepository.findAll();
    }

    @Override
    public List<Combo> getAllCombos(GetAllCombosQuery query) {
        return comboRepository.findAll();
    }

    @Override
    public List<Discount> getAllDiscounts(GetAllDiscountsQuery query) {
        return discountRepository.findAll();
    }

    @Override
    public List<Coupon> getAllCoupons(GetAllCouponsQuery query) {
        return couponRepository.findAll();
    }

    @Override
    public List<CampaignPrice> getAllCampaigns(GetAllCampaignsQuery query) {
        return campaignPriceRepository.findAll();
    }
}