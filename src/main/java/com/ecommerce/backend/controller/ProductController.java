package com.ecommerce.backend.controller;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtil jwtUtil;

    // Add product
    @PostMapping
    public ResponseEntity<Product> addProduct(
            @Valid @RequestBody Product product,
            @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.getEmailFromToken(authHeader.substring(7));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(product, email));
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // Update product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product,
            @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.getEmailFromToken(authHeader.substring(7));
        return ResponseEntity.ok(productService.updateProduct(id, product, email));
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.getEmailFromToken(authHeader.substring(7));
        productService.deleteProduct(id, email);
        return ResponseEntity.ok("Product deleted successfully!");
    }
    // Category filter
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    // My products
    @GetMapping("/my")
    public ResponseEntity<List<Product>> getMyProducts(
            @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.getEmailFromToken(authHeader.substring(7));
        return ResponseEntity.ok(productService.getMyProducts(email));
    }
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchByName(
            @RequestParam String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }

    @GetMapping("/price")
    public ResponseEntity<List<Product>> filterByPrice(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(productService.filterByPrice(min, max));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterByNameAndPrice(
            @RequestParam String name,
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(productService.filterByNameAndPrice(name, min, max));
    }
}