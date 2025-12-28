package com.ordermanagement.ordermanagement.repository;

import com.ordermanagement.ordermanagement.model.OrderItemsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItemsModel, Long> {
    List<OrderItemsModel> findByOrderId(Long orderId);
    List<OrderItemsModel> findByProductId(Long productId);
}
