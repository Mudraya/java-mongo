package com.example.pet.controller;

import com.example.pet.dto.CreateOrderRequest;
import com.example.pet.service.OrderService;
import com.example.pet.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request.getStatus(), request.getProductNames(), request.getQuantities());
    }
}
