package com.example.pet.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "Orders")
public class Order {
    @Id
    private String id;
    private String status;
    private LocalDateTime createdAt;
}