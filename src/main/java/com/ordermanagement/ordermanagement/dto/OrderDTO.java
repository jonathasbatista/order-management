package com.ordermanagement.ordermanagement.dto;

import com.ordermanagement.ordermanagement.enums.OrderStatus;
import com.ordermanagement.ordermanagement.model.OrderItemsModel;
import com.ordermanagement.ordermanagement.model.OrderModel;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        Long id,
        Long customerId,
        OrderStatus status,
        LocalDateTime createdAt,
        Long totalCents,
        List<OrderItemDTO> items
) {

    public record OrderItemDTO(
            Long productId,
            int quantity,
            int unitPriceCents
    ) {}

    public static OrderDTO from(OrderModel order, List<OrderItemsModel> items) {
        long calculatedTotal = items.stream()
                .mapToLong(item -> (long) item.getUnitPriceCents() * item.getQuantity())
                .sum();

        List<OrderItemDTO> itemDTOs = items.stream()
                .map(item -> new OrderItemDTO(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getUnitPriceCents()
                ))
                .toList();

        return new OrderDTO(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getCreatedAt(),
                calculatedTotal,
                itemDTOs
        );
    }
}