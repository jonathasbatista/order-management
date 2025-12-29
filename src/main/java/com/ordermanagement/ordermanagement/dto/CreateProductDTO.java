package com.ordermanagement.ordermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateProductDTO(
        @NotBlank(message = "O nome do produto é obrigatório")
        String name,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        int priceCents,

        @NotBlank(message = "A categoria é obrigatória")
        String category,

        @NotNull(message = "O status (ativo/inativo) é obrigatório")
        Boolean active
) {}