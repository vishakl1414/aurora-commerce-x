package com.vishal.commerce.orderService.messaging;

import com.vishal.commerce.orderService.config.RabbitMQConfig;
import com.vishal.commerce.orderService.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrderMessage(Order order) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                order
        );
        System.out.println("Message sent to RabbitMQ: Order ID " + order.getId());
    }
}