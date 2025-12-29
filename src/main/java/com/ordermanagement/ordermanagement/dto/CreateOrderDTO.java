package com.ordermanagement.ordermanagement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateOrderDTO(
        @NotNull(message = "O ID do cliente é obrigatório")
        Long customerId,

        @NotEmpty(message = "O pedido deve ter pelo menos um item")
        @Valid
        List<CreateOrderItemDTO> items
) {
    public record CreateOrderItemDTO(
            @NotNull(message = "O ID do produto é obrigatório")
            Long productId,

            @Positive(message = "A quantidade deve ser maior que zero")
            int quantity
    ) {}
}