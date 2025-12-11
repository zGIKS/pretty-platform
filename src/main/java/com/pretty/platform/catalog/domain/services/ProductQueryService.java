package com.pretty.platform.catalog.domain.services;

import com.pretty.platform.catalog.domain.model.queries.GetAllProductsQuery;
import com.pretty.platform.catalog.domain.model.queries.GetProductByIdQuery;
import com.pretty.platform.shared.domain.model.aggregates.Product;
import java.util.List;
import java.util.Optional;

/**
 * Domain service for product query operations
 */
public interface ProductQueryService {
    Optional<Product> handle(GetProductByIdQuery query);
    List<Product> handle(GetAllProductsQuery query);
}