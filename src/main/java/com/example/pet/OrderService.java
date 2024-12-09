package com.example.pet;

import com.example.pet.models.Order;
import com.example.pet.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(String status) {
        Order order = new Order();
        order.setStatus(status);
        order.setCreatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
