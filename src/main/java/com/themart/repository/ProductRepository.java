package com.themart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.themart.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
   List<Product> findByIsActiveTrue();
    List<Product> findByIsFeaturedTrue();
    List<Product> findByIsActiveTrueOrderByIdDesc();
    List<Product> findByCategory_Slug(String slug);
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByGender(String gender);
List<Product> findByGenderIn(List<String> genders);

  


@Query("SELECT p FROM Product p WHERE p.isActive = true AND (" +
       "LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
       "LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
       "LOWER(p.tags) LIKE LOWER(CONCAT('%', :q, '%')))")
List<Product> searchProducts(@Param("q") String q);
}