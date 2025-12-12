package com.pretty.platform.catalog.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 * Resource for creating a product
 */
@Schema(description = "Product creation request")
public record CreateProductResource(
    @Schema(description = "Product title", example = "Wireless Headphones")
    @NotBlank(message = "Title is required")
    String title,

    @Schema(description = "Product description", example = "High-quality wireless headphones")
    String description,

    @Schema(description = "Product brand", example = "Sony")
    @NotBlank(message = "Brand is required")
    String brand,

    @Schema(description = "Product categories", example = "[\"Electronics\", \"Audio\"]")
    List<@NotBlank String> categories,

    @Schema(description = "Product subcategories", example = "[\"Headphones\", \"Wireless\"]")
    List<@NotBlank String> subcategories,

    @Schema(description = "Product tags", example = "[\"wireless\", \"bluetooth\"]")
    List<@NotBlank String> tags,

    @Schema(description = "Product image URLs", example = "[\"https://example.com/image1.jpg\"]")
    List<@NotBlank String> imageUrls
) {}