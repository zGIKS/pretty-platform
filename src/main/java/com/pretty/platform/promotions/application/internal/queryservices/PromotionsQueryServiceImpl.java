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

    public PromotionsQueryServiceImpl(
            BasePriceRepository basePriceRepository,
            DiscountRepository discountRepository,
            CampaignPriceRepository campaignPriceRepository) {
        this.basePriceRepository = basePriceRepository;
        this.discountRepository = discountRepository;
        this.campaignPriceRepository = campaignPriceRepository;
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
    public List<Discount> getDiscountsByProduct(GetDiscountsByProductQuery query) {
        var productId = new com.pretty.platform.promotions.domain.model.valueobjects.ProductId(query.productId());
        return discountRepository.findByProductId(productId);
    }

    @Override
    public List<CampaignPrice> getCampaignsByProduct(GetCampaignsByProductQuery query) {
        var productId = new com.pretty.platform.promotions.domain.model.valueobjects.ProductId(query.productId());
        return campaignPriceRepository.findByProductId(productId);
    }

    @Override
    public List<Discount> getAllDiscounts(GetAllDiscountsQuery query) {
        return discountRepository.findAll();
    }

    @Override
    public List<CampaignPrice> getAllCampaigns(GetAllCampaignsQuery query) {
        return campaignPriceRepository.findAll();
    }
}