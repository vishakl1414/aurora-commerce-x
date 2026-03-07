package com.ecommerce.backend.repository;

import com.ecommerce.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.backend.model.User;
import java.util.List;



@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Category se filter
    List<Product> findByCategory(String category);

    // User ke products
    List<Product> findByAddedBy(User user);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByNameContainingIgnoreCaseAndPriceBetween(
            String name, Double minPrice, Double maxPrice
    );
}