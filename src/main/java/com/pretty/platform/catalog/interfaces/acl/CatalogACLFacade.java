package com.pretty.platform.catalog.interfaces.acl;

import com.pretty.platform.catalog.application.internal.queryservices.ProductQueryServiceImpl;
import com.pretty.platform.catalog.domain.model.queries.GetProductByIdQuery;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * ACL Facade for catalog bounded context
 * Provides access to catalog functionality for other bounded contexts
 */
@Component
public class CatalogACLFacade {

    private final ProductQueryServiceImpl productQueryService;

    public CatalogACLFacade(ProductQueryServiceImpl productQueryService) {
        this.productQueryService = productQueryService;
    }

    /**
     * Validates that a product exists
     * @param productId the product ID to validate
     * @return true if product exists, false otherwise
     */
    public boolean productExists(UUID productId) {
        var query = new GetProductByIdQuery(productId);
        var product = productQueryService.handle(query);
        return product.isPresent();
    }
}