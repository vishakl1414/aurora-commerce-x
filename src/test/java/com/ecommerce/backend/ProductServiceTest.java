package com.ecommerce.backend;

import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Test
    void getProductById_Success() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("iPhone");
        product.setPrice(999.99);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(1L);

        // Assert
        assertEquals("iPhone", result.getName());
        assertEquals(999.99, result.getPrice());
    }
    @Test
    void getProductById_NotFound() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(99L);
        });
    }
    @Test
    void addProduct_Success() {
        // Arrange
        User user = new User();
        user.setEmail("ahmed@gmail.com");

        Product product = new Product();
        product.setName("Samsung TV");
        product.setPrice(50000.0);

        when(userService.getUserByEmail("ahmed@gmail.com")).thenReturn(user);
        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product result = productService.addProduct(product, "ahmed@gmail.com");

        // Assert
        assertEquals("ahmed@gmail.com", result.getAddedBy().getEmail());
        assertEquals("Samsung TV", result.getName());
    }

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProductService productService;

}