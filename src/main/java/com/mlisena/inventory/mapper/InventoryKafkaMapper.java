package com.mlisena.inventory.mapper;

import com.mlisena.inventory.dto.payload.CreateInventoryPayload;
import com.mlisena.inventory.dto.CreateInventoryRequest;
import com.mlisena.inventory.model.Inventory;

public class InventoryKafkaMapper {

    private InventoryKafkaMapper() {
        // Private constructor to prevent instantiation
    }

    public static Inventory toEntity(CreateInventoryRequest request) {
        return Inventory.builder()
                .skuCode(request.skuCode())
                .quantity(request.quantity())
                .build();
    }

    public static CreateInventoryRequest toRequest(CreateInventoryPayload payload) {
        return new CreateInventoryRequest(payload.skuCode(), payload.quantity());
    }
}
