package com.mlisena.inventory.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record InventoryCreateRequest(
    @NotBlank String skuCode,
    @PositiveOrZero Integer quantity
) {
}
