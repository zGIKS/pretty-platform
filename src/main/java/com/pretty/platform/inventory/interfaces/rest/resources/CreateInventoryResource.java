package com.pretty.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Resource for creating inventory
 */
@Schema(description = "Create inventory request")
public record CreateInventoryResource(
    @Schema(description = "Product ID")
    @NotNull
    UUID productId,

    @Schema(description = "Product variant")
    @NotBlank
    String variant,

    @Schema(description = "Initial stock quantity")
    @Min(0)
    int initialStock,

    @Schema(description = "Low stock threshold")
    @Min(0)
    int lowStockThreshold
) {
}