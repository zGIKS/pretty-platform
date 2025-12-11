package com.pretty.platform.promotions.interfaces.rest;

import com.pretty.platform.promotions.application.internal.commandservices.PromotionsCommandServiceImpl;
import com.pretty.platform.promotions.application.internal.queryservices.PromotionsQueryServiceImpl;
import com.pretty.platform.promotions.domain.model.queries.*;
import com.pretty.platform.promotions.interfaces.rest.resources.*;
import com.pretty.platform.promotions.interfaces.rest.transform.*;
import com.pretty.platform.shared.domain.model.aggregates.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for promotions management
 */
@RestController
@RequestMapping("/api/v1/promotions")
@Tag(name = "Promotions", description = "Promotions management API")
public class PromotionsController {

    private final PromotionsCommandServiceImpl commandService;
    private final PromotionsQueryServiceImpl queryService;

    public PromotionsController(
            PromotionsCommandServiceImpl commandService,
            PromotionsQueryServiceImpl queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    // Base Price endpoints
    @PostMapping("/base-prices")
    @Operation(summary = "Create base price for a product")
    public ResponseEntity<Void> createBasePrice(@RequestBody CreateBasePriceResource resource) {
        var command = CreateBasePriceCommandFromResourceAssembler.toCommand(resource);
        commandService.createBasePrice(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/base-prices/{productId}")
    @Operation(summary = "Get base price for a product")
    public ResponseEntity<BasePrice> getBasePrice(@PathVariable String productId) {
        var query = new GetBasePriceQuery(UUID.fromString(productId));
        return queryService.getBasePrice(query)
            .map(price -> ResponseEntity.ok(price))
            .orElse(ResponseEntity.notFound().build());
    }

    // Discount endpoints
    @PostMapping("/discounts")
    @Operation(summary = "Create discount for a product")
    public ResponseEntity<Void> createDiscount(@RequestBody CreateDiscountResource resource) {
        var command = CreateDiscountCommandFromResourceAssembler.toCommand(resource);
        commandService.createDiscount(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/discounts/active")
    @Operation(summary = "Get all active discounts")
    public ResponseEntity<List<Discount>> getActiveDiscounts() {
        var query = new GetActiveDiscountsQuery();
        var discounts = queryService.getActiveDiscounts(query);
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/discounts/product/{productId}")
    @Operation(summary = "Get discounts for a specific product")
    public ResponseEntity<List<Discount>> getDiscountsByProduct(@PathVariable String productId) {
        var query = new GetDiscountsByProductQuery(UUID.fromString(productId));
        var discounts = queryService.getDiscountsByProduct(query);
        return ResponseEntity.ok(discounts);
    }

    // Campaign endpoints
    @PostMapping("/campaigns")
    @Operation(summary = "Create campaign price for a product")
    public ResponseEntity<Void> createCampaignPrice(@RequestBody CreateCampaignPriceResource resource) {
        var command = CreateCampaignPriceCommandFromResourceAssembler.toCommand(resource);
        commandService.createCampaignPrice(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/campaigns/active")
    @Operation(summary = "Get all active campaigns")
    public ResponseEntity<List<CampaignPrice>> getActiveCampaigns() {
        var query = new GetActiveCampaignsQuery();
        var campaigns = queryService.getActiveCampaigns(query);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/campaigns/product/{productId}")
    @Operation(summary = "Get campaigns for a specific product")
    public ResponseEntity<List<CampaignPrice>> getCampaignsByProduct(@PathVariable String productId) {
        var query = new GetCampaignsByProductQuery(UUID.fromString(productId));
        var campaigns = queryService.getCampaignsByProduct(query);
        return ResponseEntity.ok(campaigns);
    }

    // Coupon endpoints
    @PostMapping("/coupons")
    @Operation(summary = "Create coupon")
    public ResponseEntity<Void> createCoupon(@RequestBody CreateCouponResource resource) {
        var command = CreateCouponCommandFromResourceAssembler.toCommand(resource);
        commandService.createCoupon(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/coupons/{code}")
    @Operation(summary = "Get coupon by code")
    public ResponseEntity<Coupon> getCouponByCode(@PathVariable String code) {
        var query = new GetCouponByCodeQuery(code);
        return queryService.getCouponByCode(query)
            .map(coupon -> ResponseEntity.ok(coupon))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/coupons/product/{productId}")
    @Operation(summary = "Get coupons for a specific product")
    public ResponseEntity<List<Coupon>> getCouponsByProduct(@PathVariable String productId) {
        var query = new GetCouponsByProductQuery(UUID.fromString(productId));
        var coupons = queryService.getCouponsByProduct(query);
        return ResponseEntity.ok(coupons);
    }

    // Pack endpoints
    @PostMapping("/packs")
    @Operation(summary = "Create pack")
    public ResponseEntity<Void> createPack(@RequestBody CreatePackResource resource) {
        var command = CreatePackCommandFromResourceAssembler.toCommand(resource);
        commandService.createPack(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/packs/active")
    @Operation(summary = "Get all active packs")
    public ResponseEntity<List<Pack>> getActivePacks() {
        var query = new GetActivePacksQuery();
        var packs = queryService.getActivePacks(query);
        return ResponseEntity.ok(packs);
    }

    @GetMapping("/packs/{packId}")
    @Operation(summary = "Get pack by ID")
    public ResponseEntity<Pack> getPackById(@PathVariable String packId) {
        var query = new GetPackByIdQuery(UUID.fromString(packId));
        return queryService.getPackById(query)
            .map(pack -> ResponseEntity.ok(pack))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/packs/product/{productId}")
    @Operation(summary = "Get packs containing a specific product")
    public ResponseEntity<List<Pack>> getPacksByProduct(@PathVariable String productId) {
        var query = new GetPacksByProductQuery(UUID.fromString(productId));
        var packs = queryService.getPacksByProduct(query);
        return ResponseEntity.ok(packs);
    }

    // Combo endpoints
    @PostMapping("/combos")
    @Operation(summary = "Create combo")
    public ResponseEntity<Void> createCombo(@RequestBody CreateComboResource resource) {
        var command = CreateComboCommandFromResourceAssembler.toCommand(resource);
        commandService.createCombo(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/combos/active")
    @Operation(summary = "Get all active combos")
    public ResponseEntity<List<Combo>> getActiveCombos() {
        var query = new GetActiveCombosQuery();
        var combos = queryService.getActiveCombos(query);
        return ResponseEntity.ok(combos);
    }

    @GetMapping("/combos/{comboId}")
    @Operation(summary = "Get combo by ID")
    public ResponseEntity<Combo> getComboById(@PathVariable String comboId) {
        var query = new GetComboByIdQuery(UUID.fromString(comboId));
        return queryService.getComboById(query)
            .map(combo -> ResponseEntity.ok(combo))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/combos/product/{productId}")
    @Operation(summary = "Get combos containing a specific product")
    public ResponseEntity<List<Combo>> getCombosByProduct(@PathVariable String productId) {
        var query = new GetCombosByProductQuery(UUID.fromString(productId));
        var combos = queryService.getCombosByProduct(query);
        return ResponseEntity.ok(combos);
    }
}