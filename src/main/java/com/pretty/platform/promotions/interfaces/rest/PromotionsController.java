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
}