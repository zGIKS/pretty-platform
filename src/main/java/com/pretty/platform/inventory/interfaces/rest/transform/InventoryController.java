package com.pretty.platform.inventory.interfaces.rest.transform;

import com.pretty.platform.inventory.domain.model.commands.*;
import com.pretty.platform.inventory.domain.model.queries.GetAllInventoryQuery;
import com.pretty.platform.inventory.domain.model.queries.GetInventoryByProductQuery;
import com.pretty.platform.inventory.domain.model.queries.GetLowStockItemsQuery;
import com.pretty.platform.inventory.domain.services.InventoryCommandService;
import com.pretty.platform.inventory.domain.services.InventoryQueryService;
import com.pretty.platform.inventory.interfaces.rest.resources.*;
import com.pretty.platform.shared.domain.model.aggregates.Inventory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Inventory operations
 */
@RestController
@RequestMapping("/api/v1/inventories")
@Tag(name = "Inventory Management", description = "API for managing product inventory")
public class InventoryController {

    private final InventoryCommandService inventoryCommandService;
    private final InventoryQueryService inventoryQueryService;

    public InventoryController(InventoryCommandService inventoryCommandService,
                             InventoryQueryService inventoryQueryService) {
        this.inventoryCommandService = inventoryCommandService;
        this.inventoryQueryService = inventoryQueryService;
    }

    @PostMapping
    @Operation(summary = "Create inventory for a product variant")
    public ResponseEntity<Long> createInventory(@RequestBody CreateInventoryResource resource) {
        var command = new CreateInventoryCommand(
            new com.pretty.platform.inventory.domain.model.valueobjects.ProductId(resource.productId()),
            new com.pretty.platform.inventory.domain.model.valueobjects.Variant(resource.variant()),
            new com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity(resource.initialStock()),
            new com.pretty.platform.inventory.domain.model.valueobjects.Threshold(resource.lowStockThreshold())
        );

        var result = inventoryCommandService.handle(command);
        return result.map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id))
                    .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/stock")
    @Operation(summary = "Update stock levels")
    public ResponseEntity<Long> updateStock(@RequestBody UpdateStockResource resource) {
        var command = new UpdateStockCommand(
            new com.pretty.platform.inventory.domain.model.valueobjects.ProductId(resource.productId()),
            new com.pretty.platform.inventory.domain.model.valueobjects.Variant(resource.variant()),
            new com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity(resource.availableStock()),
            new com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity(resource.reservedStock()),
            new com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity(resource.soldStock())
        );

        var result = inventoryCommandService.handle(command);
        return result.map(id -> ResponseEntity.ok(id))
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reserve")
    @Operation(summary = "Reserve stock for an order")
    public ResponseEntity<Long> reserveStock(@RequestParam UUID productId,
                                           @RequestParam String variant,
                                           @RequestParam int quantity) {
        var command = new ReserveStockCommand(
            new com.pretty.platform.inventory.domain.model.valueobjects.ProductId(productId),
            new com.pretty.platform.inventory.domain.model.valueobjects.Variant(variant),
            new com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity(quantity)
        );

        var result = inventoryCommandService.handle(command);
        return result.map(id -> ResponseEntity.ok(id))
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/release")
    @Operation(summary = "Release reserved stock")
    public ResponseEntity<Long> releaseStock(@RequestParam UUID productId,
                                           @RequestParam String variant,
                                           @RequestParam int quantity) {
        var command = new ReleaseStockCommand(
            new com.pretty.platform.inventory.domain.model.valueobjects.ProductId(productId),
            new com.pretty.platform.inventory.domain.model.valueobjects.Variant(variant),
            new com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity(quantity)
        );

        var result = inventoryCommandService.handle(command);
        return result.map(id -> ResponseEntity.ok(id))
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/sell")
    @Operation(summary = "Mark stock as sold")
    public ResponseEntity<Long> sellStock(@RequestParam UUID productId,
                                        @RequestParam String variant,
                                        @RequestParam int quantity) {
        var command = new SellStockCommand(
            new com.pretty.platform.inventory.domain.model.valueobjects.ProductId(productId),
            new com.pretty.platform.inventory.domain.model.valueobjects.Variant(variant),
            new com.pretty.platform.inventory.domain.model.valueobjects.StockQuantity(quantity)
        );

        var result = inventoryCommandService.handle(command);
        return result.map(id -> ResponseEntity.ok(id))
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all inventory items")
    public ResponseEntity<List<InventoryResource>> getAllInventory() {
        var query = new GetAllInventoryQuery();
        var inventories = inventoryQueryService.handle(query);
        var resources = inventories.stream()
            .map(this::toResource)
            .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get inventory by product ID")
    public ResponseEntity<InventoryResource> getInventoryByProduct(@PathVariable UUID productId) {
        var query = new GetInventoryByProductQuery(
            new com.pretty.platform.inventory.domain.model.valueobjects.ProductId(productId)
        );
        var inventory = inventoryQueryService.handle(query);
        return inventory.map(inv -> ResponseEntity.ok(toResource(inv)))
                       .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get items with low stock")
    public ResponseEntity<List<InventoryResource>> getLowStockItems() {
        var query = new GetLowStockItemsQuery();
        var inventories = inventoryQueryService.handle(query);
        var resources = inventories.stream()
            .map(this::toResource)
            .toList();
        return ResponseEntity.ok(resources);
    }

    private InventoryResource toResource(Inventory inventory) {
        return new InventoryResource(
            inventory.getId(),
            inventory.getProductId().productId(),
            inventory.getVariant().value(),
            inventory.getAvailableStock().value(),
            inventory.getReservedStock().value(),
            inventory.getSoldStock().value(),
            inventory.getLowStockThreshold().value()
        );
    }
}