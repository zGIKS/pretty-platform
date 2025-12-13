package com.pretty.platform.catalog.interfaces.rest.transform;

import com.pretty.platform.catalog.domain.model.commands.CreateProductCommand;
import com.pretty.platform.catalog.domain.model.queries.GetProductByIdQuery;
import com.pretty.platform.catalog.domain.model.queries.GetProductsByCategoryAndTagQuery;
import com.pretty.platform.catalog.domain.model.queries.GetProductsByNameQuery;
import com.pretty.platform.catalog.domain.services.ProductCommandService;
import com.pretty.platform.catalog.domain.services.ProductQueryService;
import com.pretty.platform.catalog.interfaces.rest.resources.CreateProductResource;
import com.pretty.platform.catalog.interfaces.rest.resources.ProductResource;
import com.pretty.platform.catalog.domain.model.valueobjects.Category;
import com.pretty.platform.catalog.domain.model.valueobjects.Tag;
import com.pretty.platform.catalog.domain.model.valueobjects.ImageUrl;
import com.pretty.platform.shared.domain.model.aggregates.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for Product operations
 */
@RestController
@RequestMapping("/api/v1/products")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Products", description = "Product Catalog Management")
public class ProductController {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    public ProductController(ProductCommandService productCommandService, ProductQueryService productQueryService) {
        this.productCommandService = productCommandService;
        this.productQueryService = productQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product in the catalog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<UUID> createProduct(@Valid @RequestBody CreateProductResource resource) {
        var command = new CreateProductCommand(
            resource.title(),
            resource.description(),
            resource.brand(),
            resource.categories(),
            resource.tags(),
            resource.imageUrls(),
            resource.price(),
            resource.currency()
        );
        var productId = productCommandService.handle(command);
        return productId.map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id))
                        .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a product by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found and returned successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResource> getProductById(@PathVariable UUID id) {
        var query = new GetProductByIdQuery(id);
        var product = productQueryService.handle(query);
        return product.map(this::toResource)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tags")
    @Operation(summary = "Get all tags", description = "Retrieves all distinct tags used in the product catalog")
    @ApiResponse(responseCode = "200", description = "List of all distinct tags")
    public ResponseEntity<List<String>> getAllTags() {
        var tags = productQueryService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Retrieves all distinct categories used in the product catalog")
    @ApiResponse(responseCode = "200", description = "List of all distinct categories")
    public ResponseEntity<List<String>> getAllCategories() {
        var categories = productQueryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter products by category and/or tag",
               description = "Retrieves products filtered by category name and/or tag name. At least one filter parameter must be provided.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filtered products returned successfully"),
        @ApiResponse(responseCode = "400", description = "At least one filter parameter (category or tag) must be provided")
    })
    public ResponseEntity<List<ProductResource>> getProductsByCategoryAndTag(
            @Parameter(description = "Category name to filter by") @RequestParam(required = false) String category,
            @Parameter(description = "Tag name to filter by") @RequestParam(required = false) String tag) {

        if (category == null && tag == null) {
            return ResponseEntity.badRequest().build();
        }

        var query = new GetProductsByCategoryAndTagQuery(category, tag);
        var products = productQueryService.handle(query);
        var resources = products.stream().map(this::toResource).collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name",
               description = "Searches for products by title using case-insensitive partial matching")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results returned successfully"),
        @ApiResponse(responseCode = "400", description = "Search term cannot be empty")
    })
    public ResponseEntity<List<ProductResource>> searchProductsByName(
            @Parameter(description = "Search term for product title", required = true)
            @RequestParam String name) {

        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        var query = new GetProductsByNameQuery(name);
        var products = productQueryService.handle(query);
        var resources = products.stream().map(this::toResource).collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    private ProductResource toResource(Product product) {
        return new ProductResource(
            product.getId(),
            product.getTitle().title(),
            product.getDescription().description(),
            product.getBrand().brand(),
            product.getCategories().stream().map(Category::name).collect(Collectors.toList()),
            product.getTags().stream().map(Tag::name).collect(Collectors.toList()),
            product.getImageUrls().stream().map(ImageUrl::url).collect(Collectors.toList()),
            product.getPrice() != null ? product.getPrice().amount() : null,
            product.getPrice() != null ? product.getPrice().currency() : null,
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}