package com.pretty.platform.promotions.domain.services;

import com.pretty.platform.shared.domain.model.aggregates.*;
import com.pretty.platform.promotions.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Domain service for handling promotion queries
 */
public interface PromotionsQueryService {

    /**
     * Gets the base price for a product
     */
    Optional<BasePrice> getBasePrice(GetBasePriceQuery query);

    /**
     * Gets all active discounts
     */
    List<Discount> getActiveDiscounts(GetActiveDiscountsQuery query);

    /**
     * Gets all active campaigns
     */
    List<CampaignPrice> getActiveCampaigns(GetActiveCampaignsQuery query);

    /**
     * Gets a coupon by its code
     */
    Optional<Coupon> getCouponByCode(GetCouponByCodeQuery query);

    /**
     * Gets all active packs
     */
    List<Pack> getActivePacks(GetActivePacksQuery query);

    /**
     * Gets all active combos
     */
    List<Combo> getActiveCombos(GetActiveCombosQuery query);

    /**
     * Gets a pack by its ID
     */
    Optional<Pack> getPackById(GetPackByIdQuery query);

    /**
     * Gets a combo by its ID
     */
    Optional<Combo> getComboById(GetComboByIdQuery query);

    /**
     * Gets all packs that include a specific product
     */
    List<Pack> getPacksByProduct(GetPacksByProductQuery query);

    /**
     * Gets all combos that include a specific product
     */
    List<Combo> getCombosByProduct(GetCombosByProductQuery query);

    /**
     * Gets all discounts for a specific product
     */
    List<Discount> getDiscountsByProduct(GetDiscountsByProductQuery query);

    /**
     * Gets all coupons applicable to a specific product
     */
    List<Coupon> getCouponsByProduct(GetCouponsByProductQuery query);

    /**
     * Gets all campaigns applicable to a specific product
     */
    List<CampaignPrice> getCampaignsByProduct(GetCampaignsByProductQuery query);

    /**
     * Gets all packs
     */
    List<Pack> getAllPacks(GetAllPacksQuery query);

    /**
     * Gets all combos
     */
    List<Combo> getAllCombos(GetAllCombosQuery query);

    /**
     * Gets all discounts
     */
    List<Discount> getAllDiscounts(GetAllDiscountsQuery query);

    /**
     * Gets all coupons
     */
    List<Coupon> getAllCoupons(GetAllCouponsQuery query);

    /**
     * Gets all campaigns
     */
    List<CampaignPrice> getAllCampaigns(GetAllCampaignsQuery query);
}