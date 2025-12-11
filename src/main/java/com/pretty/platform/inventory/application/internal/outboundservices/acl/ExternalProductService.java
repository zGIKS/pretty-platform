package com.pretty.platform.inventory.application.internal.outboundservices.acl;

import com.pretty.platform.inventory.domain.model.valueobjects.ProductId;
import com.pretty.platform.catalog.interfaces.acl.CatalogContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * External Product Service
 * ACL implementation for accessing Product from catalog context
 */
@Service
public class ExternalProductService {

    private final CatalogContextFacade catalogContextFacade;

    public ExternalProductService(CatalogContextFacade catalogContextFacade) {
        this.catalogContextFacade = catalogContextFacade;
    }

    /**
     * Fetch Product by ID
     * @param productId The product ID
     * @return An Optional of ProductId if exists
     */
    public Optional<ProductId> fetchProductById(ProductId productId) {
        var exists = catalogContextFacade.productExists(productId.productId());
        return exists ? Optional.of(productId) : Optional.empty();
    }
}