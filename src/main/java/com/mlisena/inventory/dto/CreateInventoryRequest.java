package com.mlisena.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateInventoryRequest(
    @NotBlank String skuCode,
    @PositiveOrZero Integer quantity
) { }
