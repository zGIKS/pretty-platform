package com.pretty.platform.promotions.infrastructure.persistence.jpa.repositories;

import com.pretty.platform.shared.domain.model.aggregates.BasePrice;
import com.pretty.platform.promotions.domain.model.valueobjects.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for BasePrice aggregate
 */
@Repository
public interface BasePriceRepository extends JpaRepository<BasePrice, ProductId> {

    Optional<BasePrice> findByProductId(ProductId productId);

    boolean existsByProductId(ProductId productId);
}