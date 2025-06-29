package com.mlisena.inventory.mapper;

import com.mlisena.inventory.model.Inventory;
import inventory.Inventory.*;

public class InventoryGrpcMapper {

    private InventoryGrpcMapper() {
        // Private constructor to prevent instantiation
    }

    public static InventoryResponse toInventoryResponse(Inventory inventory) {
        return InventoryResponse
                .newBuilder()
                .setSkuCode(inventory.getSkuCode())
                .setQuantity(inventory.getQuantity())
                .build();
    }
}
