package com.pretty.platform.catalog.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Resource representing a product
 */
@Schema(description = "Product response")
public record ProductResource(
    @Schema(description = "Product ID", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @Schema(description = "Product title", example = "Wireless Headphones")
    String title,

    @Schema(description = "Product description", example = "High-quality wireless headphones")
    String description,

    @Schema(description = "Product brand", example = "Sony")
    String brand,

    @Schema(description = "Product categories", example = "[\"Electronics\", \"Audio\"]")
    List<String> categories,

    @Schema(description = "Product tags", example = "[\"wireless\", \"bluetooth\"]")
    List<String> tags,

    @Schema(description = "Product image URLs", example = "[\"https://example.com/image1.jpg\"]")
    List<String> imageUrls,

    @Schema(description = "Creation timestamp")
    LocalDateTime createdAt,

    @Schema(description = "Last update timestamp")
    LocalDateTime updatedAt
) {}