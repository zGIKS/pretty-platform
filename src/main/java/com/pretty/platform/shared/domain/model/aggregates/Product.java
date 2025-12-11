package com.pretty.platform.shared.domain.model.aggregates;

import com.pretty.platform.catalog.domain.model.valueobjects.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Product aggregate root
 */
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    private ProductTitle title;

    @Embedded
    private ProductDescription description;

    @Embedded
    private Brand brand;

    @Embedded
    private Price price;

    @Embedded
    private DiscountPrice discountPrice;

    @ElementCollection
    @CollectionTable(name = "product_categories", joinColumns = @JoinColumn(name = "product_id"))
    private List<Category> categories;

    @ElementCollection
    @CollectionTable(name = "product_subcategories", joinColumns = @JoinColumn(name = "product_id"))
    private List<Subcategory> subcategories;

    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    private List<Tag> tags;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    private List<ImageUrl> imageUrls;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Product() {}

    public Product(ProductTitle title, ProductDescription description, Brand brand, Price price, DiscountPrice discountPrice, List<Category> categories, List<Subcategory> subcategories, List<Tag> tags, List<ImageUrl> imageUrls) {
        this.title = title;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.discountPrice = discountPrice;
        this.categories = categories;
        this.subcategories = subcategories;
        this.tags = tags;
        this.imageUrls = imageUrls;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public ProductTitle getTitle() { return title; }
    public ProductDescription getDescription() { return description; }
    public Brand getBrand() { return brand; }
    public Price getPrice() { return price; }
    public DiscountPrice getDiscountPrice() { return discountPrice; }
    public List<Category> getCategories() { return categories; }
    public List<Subcategory> getSubcategories() { return subcategories; }
    public List<Tag> getTags() { return tags; }
    public List<ImageUrl> getImageUrls() { return imageUrls; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Business methods if needed
}