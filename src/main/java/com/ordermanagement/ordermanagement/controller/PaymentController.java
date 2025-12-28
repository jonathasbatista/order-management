package com.ordermanagement.ordermanagement.controller;

import com.ordermanagement.ordermanagement.dto.PaymentDTO;
import com.ordermanagement.ordermanagement.model.PaymentModel;
import com.ordermanagement.ordermanagement.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentModel> registerPayment(@RequestBody PaymentDTO dto) {
        PaymentModel newPayment = paymentService.registerPayment(dto);
        return ResponseEntity.ok(newPayment);
    }
}
