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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderItemsRepository orderItemsRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.productRepository = productRepository;
    }

    public List<OrderModel> findAll() {
        log.info("Listando todos os pedidos");
        return orderRepository.findAll();
    }

    public Optional<OrderDTO> findOrderDetails(Long id) {
        Optional<OrderModel> orderOptional = orderRepository.findById(id);

        if (orderOptional.isEmpty()) {
            log.warn("Pedido com ID {} não encontrado.", id);
            return Optional.empty();
        }

        OrderModel order = orderOptional.get();
        List<OrderItemsModel> items = orderItemsRepository.findByOrderId(id);

        log.info("Detalhes do pedido #{} recuperados com sucesso.", id);

        return Optional.of(OrderDTO.from(order, items));
    }

    @Transactional
    public OrderModel createOrder(CreateOrderDTO dto) {

        log.info("Iniciando criação de pedido para o Cliente ID: {}", dto.customerId());

        OrderModel order = new OrderModel();
        order.setCustomerId(dto.customerId());
        order.setStatus(OrderStatus.NEW);

        OrderModel savedOrder = orderRepository.save(order);

        List<OrderItemsModel> itemsToSave = new ArrayList<>();

        for (CreateOrderDTO.CreateOrderItemDTO itemDto : dto.items()) {

            ProductModel product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> {
                        log.error("Erro na criação do pedido: Produto ID {} não existe", itemDto.productId());
                        return new RuntimeException("Produto não encontrado: " + itemDto.productId());
                    });

            if (!product.isActive()) {
                log.error("Tentativa de venda de produto inativo: {} (ID: {})", product.getName(), product.getId());
                throw new RuntimeException("Produto inativo não pode ser vendido: " + product.getName());
            }

            OrderItemsModel itemModel = new OrderItemsModel();
            itemModel.setOrderId(savedOrder.getId());
            itemModel.setProductId(product.getId());
            itemModel.setQuantity(itemDto.quantity());
            itemModel.setUnitPriceCents(product.getPriceCents());

            itemsToSave.add(itemModel);
        }

        orderItemsRepository.saveAll(itemsToSave);

        log.info("Pedido #{} criado com sucesso! Itens processados: {}", savedOrder.getId(), itemsToSave.size());

        return savedOrder;
    }
}