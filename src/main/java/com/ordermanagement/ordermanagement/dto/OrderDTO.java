package com.ordermanagement.ordermanagement.dto;

import com.ordermanagement.ordermanagement.enums.OrderStatus;
import com.ordermanagement.ordermanagement.model.OrderItemsModel;
import com.ordermanagement.ordermanagement.model.OrderModel;

import java.util.List;

public class OrderDTO {
    private OrderModel order;
    private List<OrderItemsModel> items;
    private Long totalCents;

    public OrderDTO(OrderModel order, List<OrderItemsModel> items, Long totalCents) {
        this.order = order;
        this.items = items;
        this.totalCents = totalCents;
    }

    public OrderModel getOrder() {
        return order;
    }

    public List<OrderItemsModel> getItems() {
        return items;
    }

    public Long getTotalCents() {
        return totalCents;
    }
}
