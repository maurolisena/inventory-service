package com.mlisena.inventory.dto.response;

public record InventoryResponse(
    String skuCode,
    Integer quantity
) { }
