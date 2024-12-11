package com.example.pet.repository;

import com.example.pet.enums.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.pet.model.Order;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByStatus(OrderStatus status);
}
