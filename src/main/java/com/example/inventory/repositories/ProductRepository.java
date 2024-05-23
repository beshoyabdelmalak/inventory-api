package com.example.inventory.repositories;

import com.example.inventory.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> findByNameContainingOrBrandContaining(@Param("searchTerm") String searchTerm);

    List<Product> findByQuantityLessThan(int quantity);
}