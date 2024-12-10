package com.example.pet.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "OrderItems")
public class OrderItem {
    @Id
    private String id;
    private String productId; // Reference to Product
    private String orderId;   // Reference to Order
    private String name;
    private int quantity;
    private int pricePerUnit; // Price per unit in cents
}

