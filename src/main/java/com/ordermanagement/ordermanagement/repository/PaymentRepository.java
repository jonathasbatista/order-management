package com.ordermanagement.ordermanagement.repository;

import com.ordermanagement.ordermanagement.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {
    List<PaymentModel> findByOrderId(Long orderId);
}
