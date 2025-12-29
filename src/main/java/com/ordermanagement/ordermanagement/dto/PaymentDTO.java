package com.ordermanagement.ordermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentDTO(
        @NotNull(message = "O ID do pedido é obrigatório")
        Long orderId,

        @NotNull(message = "O valor do pagamento é obrigatório")
        @Positive(message = "O valor do pagamento deve ser maior que zero")
        int amountCents,

        @NotBlank(message = "O método de pagamento é obrigatório")
        String method
) {}