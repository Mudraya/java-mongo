package com.example.pet.service;

import com.example.pet.dto.UpdateOrderRequest;
import com.example.pet.enums.OrderStatus;
import com.example.pet.model.Order;
import com.example.pet.model.OrderItem;
import com.example.pet.model.Product;
import com.example.pet.repository.OrderItemRepository;
import com.example.pet.repository.OrderRepository;
import com.example.pet.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order createOrder(String status, List<String> productNames, List<Integer> quantities) {
        // Validate status
        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status + ". Allowed values are: AWAITING_SHIPMENT, SHIPPED.");
        }

        // Check if product names exist
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < productNames.size(); i++) {
            String productName = productNames.get(i);
            int quantity = quantities.get(i);

            // Find product by name
            Optional<Product> productOptional = productRepository.findByName(productName);
            if (productOptional.isEmpty()) {
                throw new IllegalArgumentException("Product with name '" + productName + "' does not exist.");
            }

            Product product = productOptional.get();

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setName(product.getName());
            orderItem.setQuantity(quantity);
            orderItem.setPricePerUnit(product.getPrice());
            orderItems.add(orderItem);
        }

        // Save Order
        Order order = new Order();
        order.setStatus(orderStatus);
        order.setCreatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        // Link OrderItems to the saved Order
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(savedOrder.getId());
            orderItemRepository.save(orderItem);
        }

        return savedOrder;
    }

    public Order updateOrder(String id, UpdateOrderRequest request) {
        // Fetch the existing order
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            throw new IllegalArgumentException("Order with ID " + id + " not found.");
        }

        Order existingOrder = optionalOrder.get();

        // Update fields based on the request
        if (request.getStatus() != null) {
            try {
                OrderStatus status = OrderStatus.valueOf(request.getStatus().toUpperCase());
                existingOrder.setStatus(status);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid order status: " + request.getStatus() + ". Allowed values are: AWAITING_SHIPMENT, SHIPPED.");
            }
        }

        // Save and return the updated order
        return orderRepository.save(existingOrder);
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order with ID " + id + " not found."));
    }

    public List<Order> getAllOrders(String sort) {
        List<Order> orders = orderRepository.findAll();
        if ("desc".equalsIgnoreCase(sort)) {
            return orders.stream()
                    .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                    .collect(Collectors.toList());
        } else {
            return orders.stream()
                    .sorted((o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()))
                    .collect(Collectors.toList());
        }
    }
}
