package com.pretty.platform.catalog.application.internal.queryservices;

import com.pretty.platform.catalog.domain.model.queries.GetAllProductsQuery;
import com.pretty.platform.catalog.domain.model.queries.GetProductByIdQuery;
import com.pretty.platform.catalog.domain.services.ProductQueryService;
import com.pretty.platform.catalog.infrastructure.persistence.jpa.repositories.ProductRepository;
import com.pretty.platform.shared.domain.model.aggregates.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ProductQueryService
 */
@Service
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;

    public ProductQueryServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> handle(GetProductByIdQuery query) {
        return productRepository.findById(query.productId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> handle(GetAllProductsQuery query) {
        return productRepository.findAll();
    }
}