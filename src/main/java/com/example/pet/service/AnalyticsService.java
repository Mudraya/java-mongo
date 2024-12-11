package com.example.pet.service;

import com.example.pet.enums.OrderStatus;
import com.example.pet.model.Order;
import com.example.pet.repository.OrderItemRepository;
import com.example.pet.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalyticsService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public AnalyticsService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public double calculateTotalRevenue() {
        // Fetch all orders with status SHIPPED
        List<Order> shippedOrders = orderRepository.findByStatus(OrderStatus.SHIPPED);

        // Calculate total revenue
        return shippedOrders.stream()
                .flatMap(order -> orderItemRepository.findByOrderId(order.getId()).stream())
                .mapToDouble(orderItem -> orderItem.getQuantity() * orderItem.getPricePerUnit())
                .sum();
    }

    public List<Map<String, Object>> getTopSellingProducts() {
        // Fetch all shipped orders
        List<Order> shippedOrders = orderRepository.findByStatus(OrderStatus.SHIPPED);

        // Aggregate product sales (name and total quantity sold)
        Map<String, Integer> productSales = new HashMap<>();
        shippedOrders.forEach(order ->
                orderItemRepository.findByOrderId(order.getId())
                        .forEach(item -> productSales.merge(item.getName(), item.getQuantity(), Integer::sum))
        );

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(productSales.entrySet());
        entries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<Map.Entry<String, Integer>> topEntries = entries.stream()
                .limit(3)
                .toList();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : topEntries) {
            Map<String, Object> productMap = new HashMap<>();
            productMap.put("productName", entry.getKey());
            productMap.put("quantitySold", entry.getValue());
            result.add(productMap);
        }

        return result;
    }
}

