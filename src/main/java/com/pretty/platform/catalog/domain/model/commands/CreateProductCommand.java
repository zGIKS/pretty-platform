package com.pretty.platform.catalog.domain.model.commands;

import java.util.List;

/**
 * Command to create a new product
 */
public record CreateProductCommand(
    String title,
    String description,
    String brand,
    List<String> categories,
    List<String> subcategories,
    List<String> tags,
    List<String> imageUrls
) {
    public CreateProductCommand {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand is required");
        }
        if (categories == null) {
            categories = List.of();
        }
        if (subcategories == null) {
            subcategories = List.of();
        }
        if (tags == null) {
            tags = List.of();
        }
        if (imageUrls == null) {
            imageUrls = List.of();
        }
    }
}