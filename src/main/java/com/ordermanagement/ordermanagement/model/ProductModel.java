package com.ordermanagement.ordermanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.io.Serializable;

@Entity
@Table(name = "products")
public class ProductModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 60)
    private String category;

    @Column(name = "price_cents", nullable = false)
    @Min(0)
    private int priceCents;

    @Column(nullable = false)
    private boolean active = true;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPriceCents(@Min(0) int priceCents) {
        this.priceCents = priceCents;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
