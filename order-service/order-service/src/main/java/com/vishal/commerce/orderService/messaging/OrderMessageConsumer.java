package com.vishal.commerce.orderService.messaging;

import com.vishal.commerce.orderService.config.RabbitMQConfig;
import com.vishal.commerce.orderService.model.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageConsumer {

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void consumeOrderMessage(Order order) {
        System.out.println("Message received from RabbitMQ!");
        System.out.println("Order ID: " + order.getId());
        System.out.println("User ID: " + order.getUserId());
        System.out.println("Product ID: " + order.getProductId());
        System.out.println("Total Price: " + order.getTotalPrice());
        System.out.println("Status: " + order.getStatus());
        System.out.println("-----------------------------------");
    }
}