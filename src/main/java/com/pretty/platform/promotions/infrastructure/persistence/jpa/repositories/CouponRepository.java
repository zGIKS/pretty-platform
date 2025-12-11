package com.pretty.platform.promotions.infrastructure.persistence.jpa.repositories;

import com.pretty.platform.shared.domain.model.aggregates.Coupon;
import com.pretty.platform.promotions.domain.model.valueobjects.CouponCode;
import com.pretty.platform.promotions.domain.model.valueobjects.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Coupon aggregate
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    Optional<Coupon> findByCouponCode(CouponCode couponCode);

    List<Coupon> findByProductId(ProductId productId);

    @Query("SELECT c FROM Coupon c WHERE c.productId = :productId AND c.validityPeriod.startDate <= :now AND c.validityPeriod.endDate >= :now AND (c.maxUsage IS NULL OR c.usageCount < c.maxUsage)")
    List<Coupon> findActiveCouponsByProductId(@Param("productId") ProductId productId, @Param("now") LocalDateTime now);

    @Query("SELECT c FROM Coupon c WHERE c.validityPeriod.startDate <= :now AND c.validityPeriod.endDate >= :now AND (c.maxUsage IS NULL OR c.usageCount < c.maxUsage)")
    List<Coupon> findAllActiveCoupons(@Param("now") LocalDateTime now);
}