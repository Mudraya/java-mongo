package com.example.pet.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.pet.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
}
