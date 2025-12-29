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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Transactional
    public PaymentModel registerPayment(PaymentDTO dto) {
        log.info("Iniciando processamento de pagamento para o Pedido ID: {}", dto.orderId());

        OrderModel order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> {
                    log.error("Erro ao registrar pagamento: Pedido {} não encontrado.", dto.orderId());
                    return new RuntimeException("Pedido não encontrado: " + dto.orderId());
                });

        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.CANCELED) {
            log.warn("Tentativa de pagamento rejeitada. O pedido {} já está {}", order.getId(), order.getStatus());
            throw new RuntimeException("Pedido já está finalizado ou cancelado.");
        }

        PaymentModel payment = new PaymentModel();
        payment.setOrderId(order.getId());
        payment.setAmountCents(dto.amountCents());
        payment.setMethod(dto.method());

        PaymentModel savedPayment = paymentRepository.save(payment);
        log.info("Pagamento registrado com sucesso. ID: {}, Valor: {}", savedPayment.getId(), savedPayment.getAmountCents());

        List<OrderItemsModel> items = orderItemsRepository.findByOrderId(order.getId());
        long totalOrderCents = items.stream()
                .mapToLong(item -> (long) item.getQuantity() * item.getUnitPriceCents())
                .sum();

        List<PaymentModel> allPayments = paymentRepository.findByOrderId(order.getId());
        long totalPaidCents = allPayments.stream()
                .mapToLong(PaymentModel::getAmountCents)
                .sum();

        log.info("Progresso do pagamento Pedido #{}: Pago={} / Total={}", order.getId(), totalPaidCents, totalOrderCents);

        if (totalPaidCents >= totalOrderCents) {
            log.info("O valor pago cobre o total. Atualizando status do pedido #{} para PAID.", order.getId());
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }

        return savedPayment;
    }
}