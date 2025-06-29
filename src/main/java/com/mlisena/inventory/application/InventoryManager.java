package com.mlisena.inventory.application;

import com.mlisena.inventory.dto.payload.InventoryCreatedPayload;
import com.mlisena.inventory.dto.CreateInventoryRequest;
import com.mlisena.inventory.model.Inventory;
import com.mlisena.inventory.exception.inventory.InventoryNotFoundException;
import com.mlisena.inventory.repository.InventoryRepository;
import com.mlisena.inventory.service.kafka.InventoryKafkaProducer;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryManager {

    private final InventoryRepository inventoryRepository;
    private final InventoryKafkaProducer inventoryKafkaProducer;

    private static final int BATCH_SIZE = 30;

    @Transactional
    public void createInventory(@Valid CreateInventoryRequest request) {
        log.info("Creating inventory for SKU code: {} with quantity: {}", request.skuCode(), request.quantity());
        inventoryRepository.save(
            Inventory.builder()
                .skuCode(request.skuCode())
                .quantity(request.quantity())
                .build()
        );

        log.info("Inventory created successfully for SKU code: {}", request.skuCode());
        inventoryKafkaProducer.confirmCreateInventoryEvent(
            InventoryCreatedPayload.builder()
                .skuCode(request.skuCode())
                .success(true)
                .build()
        );
    }

    public Inventory getProductInventory(String skuCode) {
        return inventoryRepository
                .findBySkuCode(skuCode)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for SKU code: " + skuCode));
    }

    public List<Inventory> getProductInventories(List<String> skuCodeList) {
        return inventoryRepository.findBySkuCodeIn(skuCodeList);
    }

    public Map<String, Inventory> getInventoriesInBatch(List<String> skuCodeList, List<Inventory> inventories) {
        Map<String, Inventory> result = new HashMap<>();
        for (int i = 0; i < skuCodeList.size(); i += BATCH_SIZE) {
            List<String> batch = skuCodeList.subList(i, Math.min(i + BATCH_SIZE, skuCodeList.size()));
            log.info("Processing batch of size: {}", batch.size());
            inventories
                .stream()
                .filter(inventory -> batch.contains(inventory.getSkuCode()))
                .forEach(inventory -> result.put(inventory.getSkuCode(), inventory));
        }
        return result;
    }

    public boolean checkProductStock(@NotBlank String skuCode, @Positive Integer quantity) {
        log.info("Checking stock for SKU code: {} with quantity: {}", skuCode, quantity);
        return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }

}
