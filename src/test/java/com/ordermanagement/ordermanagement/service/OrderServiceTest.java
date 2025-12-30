package com.ordermanagement.ordermanagement.service;

import com.ordermanagement.ordermanagement.dto.CreateOrderDTO;
import com.ordermanagement.ordermanagement.model.OrderModel;
import com.ordermanagement.ordermanagement.model.ProductModel;
import com.ordermanagement.ordermanagement.repository.OrderItemsRepository;
import com.ordermanagement.ordermanagement.repository.OrderRepository;
import com.ordermanagement.ordermanagement.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemsRepository itemsRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldThrowExceptionWhenProductIsInactive() {
        ProductModel inactiveProduct = new ProductModel();
        inactiveProduct.setActive(false);

        when(productRepository.findById(1L)).thenReturn(Optional.of(inactiveProduct));
        when(orderRepository.save(any())).thenReturn(new OrderModel());

        CreateOrderDTO.CreateOrderItemDTO itemDTO = new CreateOrderDTO.CreateOrderItemDTO(1L, 1);
        CreateOrderDTO orderDTO = new CreateOrderDTO(1L, List.of(itemDTO));

        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderDTO));
    }
}