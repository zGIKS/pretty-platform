package com.pretty.platform.inventory.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Resource for updating stock
 */
@Schema(description = "Update stock request")
public record UpdateStockResource(
    @Schema(description = "Product ID")
    @NotNull
    UUID productId,

    @Schema(description = "Product variant")
    @NotNull
    String variant,

    @Schema(description = "Available stock quantity")
    @Min(0)
    int availableStock,

    @Schema(description = "Reserved stock quantity")
    @Min(0)
    int reservedStock,

    @Schema(description = "Sold stock quantity")
    @Min(0)
    int soldStock
) {
}