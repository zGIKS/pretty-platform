package com.pretty.platform.promotions.infrastructure.persistence.jpa.repositories;

import com.pretty.platform.shared.domain.model.aggregates.Discount;
import com.pretty.platform.promotions.domain.model.valueobjects.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for Discount aggregate
 */
@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID> {

    List<Discount> findByProductId(ProductId productId);

    @Query("SELECT d FROM Discount d WHERE d.productId = :productId AND d.validityPeriod.startDate <= :now AND d.validityPeriod.endDate >= :now")
    List<Discount> findActiveDiscountsByProductId(@Param("productId") ProductId productId, @Param("now") LocalDateTime now);

    @Query("SELECT d FROM Discount d WHERE d.validityPeriod.startDate <= :now AND d.validityPeriod.endDate >= :now")
    List<Discount> findAllActiveDiscounts(@Param("now") LocalDateTime now);
}