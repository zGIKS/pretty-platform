package com.pretty.platform.catalog.infrastructure.persistence.jpa.repositories;

import com.pretty.platform.shared.domain.model.aggregates.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * JPA repository for Product aggregate
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}