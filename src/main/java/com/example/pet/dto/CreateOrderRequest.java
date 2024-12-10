package com.example.pet.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String status;
    private List<String> productNames;
    private List<Integer> quantities;
}

