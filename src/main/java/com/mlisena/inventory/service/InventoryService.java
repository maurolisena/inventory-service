package com.mlisena.inventory.service;

import com.mlisena.inventory.dto.mapper.InventoryMapper;
import com.mlisena.inventory.dto.request.CreateInventoryRequest;
import com.mlisena.inventory.dto.response.InventoryResponse;
import com.mlisena.inventory.entity.Inventory;
import com.mlisena.inventory.exception.inventory.InventoryNotFoundException;
import com.mlisena.inventory.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional
    public void createInventory(@Valid CreateInventoryRequest request) {
        log.info("Creating inventory for SKU code: {} with quantity: {}", request.skuCode(), request.quantity());
        Inventory inventory = InventoryMapper.toEntity(request);
        inventoryRepository.save(inventory);
        log.info("Inventory created successfully for SKU code: {}", request.skuCode());
    }

    public boolean checkProductStock(@NotBlank String skuCode, @Positive Integer quantity) {
        log.info("Checking stock for SKU code: {} with quantity: {}", skuCode, quantity);
        boolean isInStock = inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
        log.info("SKU code {} has stock: {}", skuCode, isInStock);
        return isInStock;
    }

    public InventoryResponse getProductInventory(@NotBlank String skuCode) {
        log.info("Getting inventory for product with SKU code: {}", skuCode);
        Inventory inventory = inventoryRepository
                .findBySkuCode(skuCode)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for SKU code: " + skuCode));

        log.info("Inventory found for SKU code: {} with quantity: {}", skuCode, inventory.getQuantity());
        return InventoryMapper.toResponse(inventory);
    }

    public List<InventoryResponse> getProductInventories(@NotEmpty List<String> skuCodeList) {
        log.info("Getting inventory for product to SKU code list");
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodeList);

        if (inventories.isEmpty()) {
            log.warn("No inventory records found for give SKU code list");
            return List.of();
        }

        final int batchSize = 30;
        List<InventoryResponse> response = new ArrayList<>();

        for (int i = 0; i < skuCodeList.size(); i += batchSize) {
            List<String> batch = skuCodeList.subList(i, Math.min(i + batchSize, skuCodeList.size()));
            log.info("Processing batch of size: {}", batch.size());

            response.addAll(
                inventories.stream()
                    .map(InventoryMapper::toResponse)
                    .toList()
            );
        }

        return response;
    }
}
