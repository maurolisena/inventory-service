package com.mlisena.inventory.controller;

import com.mlisena.inventory.service.InventoryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@RefreshScope
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<Boolean> isInStock(
            @RequestParam @NotBlank String skuCode,
            @RequestParam @Positive @NotNull Integer quantity
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.isInStock(skuCode, quantity));
    }
}
