package com.example.inventory_service.utils;


import com.example.inventory_service.model.entities.Inventory;
import com.example.inventory_service.repositories.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;
        @Override
        public void run(String... args) throws Exception {

            var inventoryCount = inventoryRepository.count();

            if (inventoryCount > 0) {
                log.info("Data already loaded. Inventory count: {}", inventoryCount);
                return;
            }

            log.info("Loading data...");

            inventoryRepository.save(Inventory.builder().sku("0001").quantity(10).build());
            inventoryRepository.save(Inventory.builder().sku("0002").quantity(5).build());
            inventoryRepository.save(Inventory.builder().sku("0003").quantity(3).build());
            inventoryRepository.save(Inventory.builder().sku("0004").quantity(0).build());

            log.info("Data loaded. Inventory count: {}", inventoryRepository.count());
        }
}
