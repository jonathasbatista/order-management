package com.ordermanagement.ordermanagement.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "orders_items")
public class OrderItemsModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column()
    private int quantity;

    @Column()
    private int unitPriceCents;

    public Long getId() {
        return id;
    }

    public Long getOrder() {
        return orderId;
    }

    public Long getProduct() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPriceCents() {
        return unitPriceCents;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPriceCents(int unitPriceCents) {
        this.unitPriceCents = unitPriceCents;
    }
}
