package com.ordermanagement.ordermanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class PaymentModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private String method;

    @Column(name = "amount_cents", nullable = false)
    @Min(0)
    private int amountCents;

    @Column(name = "paid_at")
    private LocalDateTime paidAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getMethod() {
        return method;
    }

    @Min(0)
    public int getAmountCents() {
        return amountCents;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setAmountCents(@Min(0) int amountCents) {
        this.amountCents = amountCents;
    }
}
