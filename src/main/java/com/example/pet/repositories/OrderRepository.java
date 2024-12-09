package com.example.pet.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.pet.models.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
}
