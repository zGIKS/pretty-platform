package com.pretty.platform.catalog.application.acl;

import com.pretty.platform.catalog.domain.model.queries.GetProductByIdQuery;
import com.pretty.platform.catalog.domain.services.ProductQueryService;
import com.pretty.platform.catalog.interfaces.acl.CatalogContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of CatalogContextFacade
 */
@Service
public class CatalogContextFacadeImpl implements CatalogContextFacade {

    private final ProductQueryService productQueryService;

    public CatalogContextFacadeImpl(ProductQueryService productQueryService) {
        this.productQueryService = productQueryService;
    }

    @Override
    public boolean productExists(UUID productId) {
        var query = new GetProductByIdQuery(productId);
        return productQueryService.handle(query).isPresent();
    }
}