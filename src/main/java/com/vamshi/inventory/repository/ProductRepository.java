package com.vamshi.inventory.repository;

import com.vamshi.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    List<Product> findByCategory(String category);

    List<Product> findByNameContainingIgnoreCase(String name);

    // Custom query: find low-stock items (useful for inventory alerts)
    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
    List<Product> findLowStockProducts(int threshold);
}
