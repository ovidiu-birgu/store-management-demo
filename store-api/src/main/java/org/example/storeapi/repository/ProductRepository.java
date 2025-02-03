package org.example.storeapi.repository;

import org.example.storeapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Returns all products with stock quantity greater than the provided value, paginated
    Page<Product> findByStockQuantityGreaterThan(int stockQuantity, Pageable pageable);

    // Returns an Optional product by id, only if its stock quantity is greater than the provided value
    Optional<Product> findByIdAndStockQuantityGreaterThan(Long id, int quantity);
}
