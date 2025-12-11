package com.pretty.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Resource for Inventory operations
 */
@Schema(description = "Inventory data")
public record InventoryResource(
    @Schema(description = "Inventory ID")
    Long id,

    @Schema(description = "Product ID")
    @NotNull
    UUID productId,

    @Schema(description = "Product variant")
    @NotBlank
    String variant,

    @Schema(description = "Available stock quantity")
    @Min(0)
    int availableStock,

    @Schema(description = "Reserved stock quantity")
    @Min(0)
    int reservedStock,

    @Schema(description = "Sold stock quantity")
    @Min(0)
    int soldStock,

    @Schema(description = "Low stock threshold")
    @Min(0)
    int lowStockThreshold
) {
}