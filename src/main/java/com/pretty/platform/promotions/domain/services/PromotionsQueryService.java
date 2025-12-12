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
     * Gets all discounts for a specific product
     */
    List<Discount> getDiscountsByProduct(GetDiscountsByProductQuery query);

    /**
     * Gets all discounts
     */
    List<Discount> getAllDiscounts(GetAllDiscountsQuery query);
}