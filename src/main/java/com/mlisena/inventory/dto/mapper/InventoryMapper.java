package com.mlisena.inventory.dto.mapper;

import com.mlisena.inventory.dto.request.CreateInventoryRequest;
import com.mlisena.inventory.entity.Inventory;

public class InventoryMapper {

    private InventoryMapper() {
        // Private constructor to prevent instantiation
    }

    public static Inventory toEntity(CreateInventoryRequest request) {
        return Inventory.builder()
                .skuCode(request.skuCode())
                .quantity(request.quantity())
                .build();
    }
}
