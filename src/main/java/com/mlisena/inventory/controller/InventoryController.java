package com.mlisena.inventory.controller;

import com.mlisena.inventory.dto.request.CreateInventoryRequest;
import com.mlisena.inventory.service.InventoryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@RefreshScope
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Void> createInventory(@RequestBody CreateInventoryRequest request) {
        inventoryService.createInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Boolean> isInStock(
            @RequestParam @NotBlank String skuCode,
            @RequestParam @Positive @NotNull Integer quantity
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.isInStock(skuCode, quantity));
    }
}
