package com.ordermanagement.ordermanagement.controller;

import com.ordermanagement.ordermanagement.dto.CreateOrderDTO;
import com.ordermanagement.ordermanagement.dto.OrderDTO;
import com.ordermanagement.ordermanagement.model.OrderModel;
import com.ordermanagement.ordermanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderModel> listOrders() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable Long id) {
        return orderService.findOrderDetails(id)
                .map(dto -> ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderModel> createOrder(@RequestBody CreateOrderDTO dto) {
        OrderModel newOrder = orderService.createOrder(dto);
        return ResponseEntity.ok(newOrder);
    }
}
