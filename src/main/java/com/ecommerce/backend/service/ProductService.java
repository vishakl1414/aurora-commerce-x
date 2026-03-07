package com.ecommerce.backend.service;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.backend.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    // Add product — user ke saath
    public Product addProduct(Product product, String email) {
        logger.info("Adding product: {} by user: {}", product.getName(), email);
        User user = userService.getUserByEmail(email);
        product.setAddedBy(user);
        Product saved = productRepository.save(product);
        logger.info("Product added successfully with id: {}", saved.getId());
        return saved;
    }
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    // Get all products
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        logger.info("Total products found: {}", products.size());
        return products;
    }

    // Get product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    // Update product
    public Product updateProduct(Long id, Product updatedProduct, String email) {
        logger.info("Updating product id: {} by user: {}", id, email);
        Product existing = getProductById(id);

        if (!existing.getAddedBy().getEmail().equals(email)) {
            logger.warn("Unauthorized update attempt on product id: {} by user: {}", id, email);
            throw new UnauthorizedException("You can only update your own products!");
        }

        existing.setName(updatedProduct.getName());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCategory(updatedProduct.getCategory());
        Product updated = productRepository.save(existing);
        logger.info("Product updated successfully id: {}", updated.getId());
        return updated;
    }

    // Delete product
    public void deleteProduct(Long id, String email) {
        logger.info("Deleting product id: {} by user: {}", id, email);
        Product existing = getProductById(id);

        if (!existing.getAddedBy().getEmail().equals(email)) {
            logger.warn("Unauthorized delete attempt on product id: {} by user: {}", id, email);
            throw new UnauthorizedException("You can only delete your own products!");
        }

        productRepository.deleteById(id);
        logger.info("Product deleted successfully id: {}", id);
    }

    // Category se filter
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    // My products — token wale user ke products
    public List<Product> getMyProducts(String email) {
        User user = userService.getUserByEmail(email);
        return productRepository.findByAddedBy(user);
    }
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> filterByPrice(Double min, Double max) {
        return productRepository.findByPriceBetween(min, max);
    }

    public List<Product> filterByNameAndPrice(String name, Double min, Double max) {
        return productRepository.findByNameContainingIgnoreCaseAndPriceBetween(name, min, max);
    }

}