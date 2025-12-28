package com.ordermanagement.ordermanagement.service;

import com.ordermanagement.ordermanagement.dto.CreateOrderDTO;
import com.ordermanagement.ordermanagement.dto.OrderDTO;
import com.ordermanagement.ordermanagement.enums.OrderStatus;
import com.ordermanagement.ordermanagement.model.OrderItemsModel;
import com.ordermanagement.ordermanagement.model.OrderModel;
import com.ordermanagement.ordermanagement.model.ProductModel;
import com.ordermanagement.ordermanagement.repository.OrderItemsRepository;
import com.ordermanagement.ordermanagement.repository.OrderRepository;
import com.ordermanagement.ordermanagement.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<OrderModel> findAll() {
        return orderRepository.findAll();
    }

    public Optional<OrderDTO> findOrderDetails(Long id) {

        Optional<OrderModel> orderOptional = orderRepository.findById(id);

        if (orderOptional.isEmpty()) {
            return Optional.empty();
        }

        OrderModel order = orderOptional.get();
        List<OrderItemsModel> items = orderItemsRepository.findByOrderId(id);
        long total = 0;

        for (OrderItemsModel item : items) {
            total += (long) item.getQuantity() * item.getUnitPriceCents();
        }

        return Optional.of(new OrderDTO(order, items, total));
    }

    @Transactional
    public OrderModel createOrder(CreateOrderDTO dto) {

        OrderModel order = new OrderModel();
        order.setCustomerId(dto.getCustomerId());
        order.setStatus(OrderStatus.NEW);

        OrderModel savedOrder = orderRepository.save(order);

        for (CreateOrderDTO.CreateOrderItemDTO itemDto : dto.getItems()) {

            ProductModel product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDto.getProductId()));

            if (!product.isActive()) {
                throw new RuntimeException("Produto inativo não pode ser vendido: " + product.getName());
            }

            OrderItemsModel itemModel = new OrderItemsModel();
            itemModel.setOrderId(savedOrder.getId());
            itemModel.setProductId(product.getId());
            itemModel.setQuantity(itemDto.getQuantity());
            itemModel.setUnitPriceCents(product.getPriceCents());

            orderItemsRepository.save(itemModel);
        }

        return savedOrder;
    }
}
