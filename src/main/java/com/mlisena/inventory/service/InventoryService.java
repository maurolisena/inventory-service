package com.mlisena.inventory.service;

import com.mlisena.inventory.dto.mapper.InventoryMapper;
import com.mlisena.inventory.dto.request.CreateInventoryRequest;
import com.mlisena.inventory.entity.Inventory;
import com.mlisena.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity) {
        log.info("Checking stock for SKU code: {} with quantity: {}", skuCode, quantity);
        boolean isInStock = inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
        log.info("SKU code {} has stock: {}", skuCode, isInStock);
        return isInStock;
    }

    public void createInventory(CreateInventoryRequest request) {
        log.info("Creating inventory for SKU code: {} with quantity: {}", request.skuCode(), request.quantity());
        Inventory inventory = InventoryMapper.toEntity(request);
        inventoryRepository.save(inventory);
        log.info("Inventory created successfully for SKU code: {}", request.skuCode());
    }
}
