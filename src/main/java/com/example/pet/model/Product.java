package com.example.pet.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "Products")
public class Product {
    @Id
    private String id;
    private String name;
    private int price; // Price in cents
    private String createdAt;
    private String updatedAt;
}

