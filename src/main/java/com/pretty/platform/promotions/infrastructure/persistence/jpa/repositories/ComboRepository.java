package com.pretty.platform.promotions.infrastructure.persistence.jpa.repositories;

import com.pretty.platform.shared.domain.model.aggregates.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for Combo aggregate
 */
@Repository
public interface ComboRepository extends JpaRepository<Combo, UUID> {

    @Query("SELECT c FROM Combo c WHERE :productId MEMBER OF c.productIds")
    List<Combo> findByProductId(@Param("productId") UUID productId);

    @Query("SELECT c FROM Combo c WHERE :productId MEMBER OF c.productIds AND c.validityPeriod.startDate <= :now AND c.validityPeriod.endDate >= :now")
    List<Combo> findActiveCombosByProductId(@Param("productId") UUID productId, @Param("now") LocalDateTime now);

    @Query("SELECT c FROM Combo c WHERE c.validityPeriod.startDate <= :now AND c.validityPeriod.endDate >= :now")
    List<Combo> findAllActiveCombos(@Param("now") LocalDateTime now);
}