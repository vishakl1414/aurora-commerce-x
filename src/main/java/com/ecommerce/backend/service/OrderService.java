package com.ecommerce.backend.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    // Place order
    public Order placeOrder(Long productId, String email) {
        logger.info("Placing order for product id: {} by user: {}", productId, email);
        User user = userService.getUserByEmail(email);
        Product product = productService.getProductById(productId);

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setStatus("PENDING");
        order.setPaymentStatus("PENDING");

        Order saved = orderRepository.save(order);
        logger.info("Order placed successfully with id: {}", saved.getId());
        return saved;
    }
    public Order updatePaymentStatus(Long orderId, String paymentStatus) {
        logger.info("Updating payment status for order id: {} to: {}", orderId, paymentStatus);
        Order order = getOrderById(orderId);
        order.setPaymentStatus(paymentStatus);
        Order updated = orderRepository.save(order);
        logger.info("Payment status updated successfully for order id: {}", updated.getId());
        return updated;
    }

    // Get my orders
    public List<Order> getMyOrders(String email) {
        User user = userService.getUserByEmail(email);
        return orderRepository.findByUser(user);
    }

    // Get order by ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    // Update order status
    public Order updateOrderStatus(Long id, String status) {
        logger.info("Updating order status for id: {} to: {}", id, status);
        Order order = getOrderById(id);
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        logger.info("Order status updated successfully id: {}", updated.getId());
        return updated;
    }
}