package com.mlisena.inventory.dto.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InventoryCreatedConfirmation(
        @NotBlank String skuCode,
        @NotNull Boolean success
) { }
