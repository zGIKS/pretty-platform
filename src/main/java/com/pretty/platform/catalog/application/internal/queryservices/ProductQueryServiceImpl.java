package com.pretty.platform.catalog.application.internal.queryservices;

import com.pretty.platform.catalog.domain.model.queries.GetProductByIdQuery;
import com.pretty.platform.catalog.domain.model.queries.GetProductsByCategoryAndTagQuery;
import com.pretty.platform.catalog.domain.model.queries.GetProductsByNameQuery;
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
    public List<Product> handle(GetProductsByCategoryAndTagQuery query) {
        String category = query.category();
        String tag = query.tag();

        if (category != null && tag != null) {
            return productRepository.findByCategoryNameAndTagName(category, tag);
        } else if (category != null) {
            return productRepository.findByCategoryNameOnly(category);
        } else {
            return productRepository.findByTagNameOnly(tag);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> handle(GetProductsByNameQuery query) {
        return productRepository.findByTitleContaining(query.searchTerm());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllTags() {
        return productRepository.findAllDistinctTags();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return productRepository.findAllDistinctCategories();
    }
}