package com.ordermanagement.ordermanagement.service;

import com.ordermanagement.ordermanagement.dto.PaymentDTO;
import com.ordermanagement.ordermanagement.enums.OrderStatus;
import com.ordermanagement.ordermanagement.model.OrderItemsModel;
import com.ordermanagement.ordermanagement.model.OrderModel;
import com.ordermanagement.ordermanagement.model.PaymentModel;
import com.ordermanagement.ordermanagement.repository.OrderItemsRepository;
import com.ordermanagement.ordermanagement.repository.OrderRepository;
import com.ordermanagement.ordermanagement.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemsRepository itemsRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldChangeStatusToPaidWhenFullyPaid() {
        OrderModel order = new OrderModel();
        order.setStatus(OrderStatus.NEW);

        OrderItemsModel item = new OrderItemsModel();
        item.setQuantity(1);
        item.setUnitPriceCents(5000);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any())).thenReturn(new PaymentModel());
        when(itemsRepository.findByOrderId(any())).thenReturn(List.of(item));

        PaymentModel currentPayment = new PaymentModel();
        currentPayment.setAmountCents(5000);

        when(paymentRepository.findByOrderId(any())).thenReturn(List.of(currentPayment));

        PaymentDTO paymentDTO = new PaymentDTO(1L, 5000, "PIX");

        paymentService.registerPayment(paymentDTO);

        assertEquals(OrderStatus.PAID, order.getStatus());
    }
}