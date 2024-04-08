package com.example.inventory_service.controllers;

import com.example.inventory_service.model.dtos.OrderItemRequest;
import com.example.inventory_service.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public boolean inStock(@PathVariable String sku) {

        return inventoryService.isInStock(sku);
    }

    @PostMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public boolean inStock(@RequestBody List<OrderItemRequest> orderItemsRequest) {

        return inventoryService.areInStock(orderItemsRequest);
    }
}
