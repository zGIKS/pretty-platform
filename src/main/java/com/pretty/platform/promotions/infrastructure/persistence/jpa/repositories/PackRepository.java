package com.pretty.platform.promotions.infrastructure.persistence.jpa.repositories;

import com.pretty.platform.shared.domain.model.aggregates.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for Pack aggregate
 */
@Repository
public interface PackRepository extends JpaRepository<Pack, UUID> {

    @Query("SELECT p FROM Pack p WHERE :productId MEMBER OF p.productIds")
    List<Pack> findByProductId(@Param("productId") UUID productId);

    @Query("SELECT p FROM Pack p WHERE :productId MEMBER OF p.productIds AND p.validityPeriod.startDate <= :now AND p.validityPeriod.endDate >= :now")
    List<Pack> findActivePacksByProductId(@Param("productId") UUID productId, @Param("now") LocalDateTime now);

    @Query("SELECT p FROM Pack p WHERE p.validityPeriod.startDate <= :now AND p.validityPeriod.endDate >= :now")
    List<Pack> findAllActivePacks(@Param("now") LocalDateTime now);
}