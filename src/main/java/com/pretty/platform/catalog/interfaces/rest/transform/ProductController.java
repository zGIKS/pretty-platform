package com.pretty.platform.catalog.interfaces.rest.transform;

import com.pretty.platform.catalog.domain.model.commands.CreateProductCommand;
import com.pretty.platform.catalog.domain.model.queries.GetAllProductsQuery;
import com.pretty.platform.catalog.domain.model.queries.GetProductByIdQuery;
import com.pretty.platform.catalog.domain.services.ProductCommandService;
import com.pretty.platform.catalog.domain.services.ProductQueryService;
import com.pretty.platform.catalog.interfaces.rest.resources.CreateProductResource;
import com.pretty.platform.catalog.interfaces.rest.resources.ProductResource;
import com.pretty.platform.catalog.domain.model.valueobjects.Category;
import com.pretty.platform.catalog.domain.model.valueobjects.Subcategory;
import com.pretty.platform.catalog.domain.model.valueobjects.Tag;
import com.pretty.platform.catalog.domain.model.valueobjects.ImageUrl;
import com.pretty.platform.shared.domain.model.aggregates.Product;
import io.swagger.v3.oas.annotations.Operation;
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
            resource.subcategories(),
            resource.tags(),
            resource.imageUrls()
        );
        var productId = productCommandService.handle(command);
        return productId.map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id))
                        .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResource> getProductById(@PathVariable UUID id) {
        var query = new GetProductByIdQuery(id);
        var product = productQueryService.handle(query);
        return product.map(this::toResource)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves all products in the catalog")
    @ApiResponse(responseCode = "200", description = "List of products")
    public ResponseEntity<List<ProductResource>> getAllProducts() {
        var query = new GetAllProductsQuery();
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
            product.getSubcategories().stream().map(Subcategory::name).collect(Collectors.toList()),
            product.getTags().stream().map(Tag::name).collect(Collectors.toList()),
            product.getImageUrls().stream().map(ImageUrl::url).collect(Collectors.toList()),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}