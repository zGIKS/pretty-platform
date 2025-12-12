package com.pretty.platform.promotions.domain.services;

import com.pretty.platform.promotions.domain.model.commands.*;

/**
 * Domain service for handling promotion commands
 */
public interface PromotionsCommandService {

    /**
     * Creates a base price for a product
     */
    void createBasePrice(CreateBasePriceCommand command);

    /**
     * Creates a discount for a product
     */
    void createDiscount(CreateDiscountCommand command);

    /**
     * Creates a campaign price for a product
     */
    void createCampaignPrice(CreateCampaignPriceCommand command);
}