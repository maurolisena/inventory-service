package com.mlisena.inventory.exception.inventory;

import jakarta.validation.constraints.NotBlank;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(@NotBlank String s) {
        super(s);
    }
}
