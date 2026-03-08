package com.vishal.commerce.orderService.service;

import com.vishal.commerce.orderService.messaging.OrderMessageProducer;
import com.vishal.commerce.orderService.model.Order;
import com.vishal.commerce.orderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMessageProducer orderMessageProducer;

    // Place new order
    public Order placeOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        orderMessageProducer.sendOrderMessage(savedOrder);
        return savedOrder;
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get order by id
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    // Get orders by userId
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Update order status
    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(com.vishal.commerce.orderService.model.OrderStatus.valueOf(status));
        return orderRepository.save(order);
    }

    // Cancel order
    public void cancelOrder(Long id) {
        Order order = getOrderById(id);
        order.setStatus(com.vishal.commerce.orderService.model.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}