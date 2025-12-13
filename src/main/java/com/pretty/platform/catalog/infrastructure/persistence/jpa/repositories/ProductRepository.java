package com.pretty.platform.catalog.infrastructure.persistence.jpa.repositories;

import com.pretty.platform.shared.domain.model.aggregates.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * JPA repository for Product aggregate
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Find all distinct tags used in products
     */
    @Query("SELECT DISTINCT t.name FROM Product p JOIN p.tags t")
    List<String> findAllDistinctTags();

    /**
     * Find all distinct categories used in products
     */
    @Query("SELECT DISTINCT c.name FROM Product p JOIN p.categories c")
    List<String> findAllDistinctCategories();

    /**
     * Find products by category name
     */
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.name = :categoryName")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);

    /**
     * Find products by tag name
     */
    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t.name = :tagName")
    List<Product> findByTagName(@Param("tagName") String tagName);

    /**
     * Find products by both category and tag
     */
    @Query("SELECT DISTINCT p FROM Product p JOIN p.categories c JOIN p.tags t WHERE c.name = :categoryName AND t.name = :tagName")
    List<Product> findByCategoryNameAndTagName(@Param("categoryName") String categoryName, @Param("tagName") String tagName);

    /**
     * Find products by category only when tag is not provided
     */
    @Query("SELECT DISTINCT p FROM Product p JOIN p.categories c WHERE c.name = :categoryName")
    List<Product> findByCategoryNameOnly(@Param("categoryName") String categoryName);

    /**
     * Find products by tag only when category is not provided
     */
    @Query("SELECT DISTINCT p FROM Product p JOIN p.tags t WHERE t.name = :tagName")
    List<Product> findByTagNameOnly(@Param("tagName") String tagName);

    /**
     * Find products by title containing search term (case insensitive)
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.title.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> findByTitleContaining(@Param("searchTerm") String searchTerm);
}