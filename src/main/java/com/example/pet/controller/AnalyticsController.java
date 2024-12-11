package com.example.pet.controller;

import com.example.pet.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/total-revenue")
    public double getTotalRevenue() {
        return analyticsService.calculateTotalRevenue();
    }

    @GetMapping("/top-selling")
    public List<Map<String, Object>> getTopSellingProducts() {
        return analyticsService.getTopSellingProducts();
    }
}

