package com.example.pet.service;

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
}
