package com.example.pet.controller;

import com.example.pet.dto.CreateOrderRequest;
import com.example.pet.dto.UpdateOrderRequest;
import com.example.pet.service.OrderService;
import com.example.pet.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable String id, @RequestBody UpdateOrderRequest request) {
        return orderService.updateOrder(id, request);
    }

    @GetMapping
    public Order getOrderById(@RequestParam String id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/all")
    public List<Order> getAllOrders(@RequestParam(defaultValue = "asc") String sort) {
        return orderService.getAllOrders(sort);
    }
}
