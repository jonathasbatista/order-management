package com.ordermanagement.ordermanagement.service;

import com.ordermanagement.ordermanagement.dto.PaymentDTO;
import com.ordermanagement.ordermanagement.enums.OrderStatus;
import com.ordermanagement.ordermanagement.model.OrderItemsModel;
import com.ordermanagement.ordermanagement.model.OrderModel;
import com.ordermanagement.ordermanagement.model.PaymentModel;
import com.ordermanagement.ordermanagement.repository.OrderItemsRepository;
import com.ordermanagement.ordermanagement.repository.OrderRepository;
import com.ordermanagement.ordermanagement.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Transactional
    public PaymentModel registerPayment(PaymentDTO dto) {

        OrderModel order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + dto.getOrderId()));

        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.CANCELED) {
            throw new RuntimeException("Pedido já está finalizado ou cancelado.");
        }

        PaymentModel payment = new PaymentModel();
        payment.setOrderId(order.getId());
        payment.setAmountCents(dto.getAmountCents());

        PaymentModel savedPayment = paymentRepository.save(payment);

        List<OrderItemsModel> items = orderItemsRepository.findByOrderId(order.getId());
        long totalOrderCents = items.stream()
                .mapToLong(item -> (long) item.getQuantity() * item.getUnitPriceCents())
                .sum();

        List<PaymentModel> allPayments = paymentRepository.findByOrderId(order.getId());
        long totalPaidCents = allPayments.stream()
                .mapToLong(PaymentModel::getAmountCents)
                .sum();

        if (totalPaidCents >= totalOrderCents) {
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }

        return savedPayment;
    }
}