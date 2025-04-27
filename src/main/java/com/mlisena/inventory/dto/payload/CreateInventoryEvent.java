package com.mlisena.inventory.dto.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateInventoryEvent(
    @NotBlank String skuCode,
    @PositiveOrZero Integer quantity
) {
}
