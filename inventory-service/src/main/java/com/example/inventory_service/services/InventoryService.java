package com.example.inventory_service.services;

import com.example.inventory_service.model.dtos.OrderItemRequest;
import com.example.inventory_service.model.entities.Inventory;
import com.example.inventory_service.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;


    public boolean isInStock(String sku) {
        return inventoryRepository.findBySku(sku)
                .map(inventory -> inventory.getQuantity() > 0)
                .orElse(false);
    }

    public boolean areInStock(List<OrderItemRequest> orderItemsRequest) {

        List<String> skuWithOutStock = new ArrayList<>();

        List<String> skuList = orderItemsRequest.stream()
                .map(OrderItemRequest::getSku)
                .toList();

        List<Inventory> inventoryList = inventoryRepository.findBySkuIn(skuList);

        orderItemsRequest.forEach( orderItem -> {
            Inventory inventory = inventoryList.stream()
                    .filter(value -> value.getSku().equals(orderItem.getSku()))
                    .findFirst()
                    .orElse(null);

            if (inventory == null || inventory.getQuantity() < orderItem.getQuantity()) {
                String message = String.format("Product with SKU: %s is not in stock", orderItem.getSku());
                skuWithOutStock.add(message);
            }
        });

        log.info("Products out of stock: {}", skuWithOutStock);

        return skuWithOutStock.isEmpty();
    }
}
