package com.pretty.platform.catalog.application.internal.commandservices;

import com.pretty.platform.catalog.domain.model.commands.CreateProductCommand;
import com.pretty.platform.catalog.domain.services.ProductCommandService;
import com.pretty.platform.catalog.infrastructure.persistence.jpa.repositories.ProductRepository;
import com.pretty.platform.shared.domain.model.aggregates.Product;
import com.pretty.platform.catalog.domain.model.valueobjects.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of ProductCommandService
 */
@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepository productRepository;

    public ProductCommandServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Optional<UUID> handle(CreateProductCommand command) {
        var title = new ProductTitle(command.title());
        var description = new ProductDescription(command.description());
        var brand = new Brand(command.brand());
        var price = new Price(command.price());
        var discountPrice = new DiscountPrice(command.discountPrice());
        var categories = command.categories().stream().map(Category::new).collect(Collectors.toList());
        var subcategories = command.subcategories().stream().map(Subcategory::new).collect(Collectors.toList());
        var tags = command.tags().stream().map(Tag::new).collect(Collectors.toList());
        var imageUrls = command.imageUrls().stream().map(ImageUrl::new).collect(Collectors.toList());

        var product = new Product(title, description, brand, price, discountPrice, categories, subcategories, tags, imageUrls);
        var savedProduct = productRepository.save(product);
        return Optional.of(savedProduct.getId());
    }
}