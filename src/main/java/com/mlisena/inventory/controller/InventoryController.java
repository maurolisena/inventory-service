package com.mlisena.inventory.controller;

import com.mlisena.inventory.dto.request.CreateInventoryRequest;
import com.mlisena.inventory.dto.response.InventoryResponse;
import com.mlisena.inventory.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@RefreshScope
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Void> createInventory(@RequestBody @Valid CreateInventoryRequest request) {
        inventoryService.createInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{skuCode}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable @NotBlank String skuCode) {
        InventoryResponse inventory = inventoryService.getProductInventory(skuCode);
        return ResponseEntity.status(HttpStatus.OK).body(inventory);
    }

    @PostMapping("/list")
    public ResponseEntity<List<InventoryResponse>> getInventories(@RequestBody List<String> skuCodeList) {
        List<InventoryResponse> inventories = inventoryService.getProductInventories(skuCodeList);
        return ResponseEntity.status(HttpStatus.OK).body(inventories);
    }

    @GetMapping("/check-stock")
    public ResponseEntity<Boolean> checkStock(
            @RequestParam @NotBlank String skuCode,
            @RequestParam @Positive @NotNull Integer quantity
    ) {
        boolean checkProductStock = inventoryService.checkProductStock(skuCode, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(checkProductStock);
    }
}
