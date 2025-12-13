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

    @ElementCollection
    @CollectionTable(name = "product_categories", joinColumns = @JoinColumn(name = "product_id"))
    private List<Category> categories;

    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    private List<Tag> tags;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    private List<ImageUrl> imageUrls;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Price price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Product() {}

    public Product(ProductTitle title, ProductDescription description, Brand brand, List<Category> categories, List<Tag> tags, List<ImageUrl> imageUrls, Price price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        this.title = title;
        this.description = description;
        this.brand = brand;
        this.categories = categories;
        this.tags = tags;
        this.imageUrls = imageUrls;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public ProductTitle getTitle() { return title; }
    public ProductDescription getDescription() { return description; }
    public Brand getBrand() { return brand; }
    public List<Category> getCategories() { return categories; }
    public List<Tag> getTags() { return tags; }
    public List<ImageUrl> getImageUrls() { return imageUrls; }
    public Price getPrice() { return price; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Business methods if needed
}