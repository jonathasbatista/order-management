package com.ordermanagement.ordermanagement.controller;

import com.ordermanagement.ordermanagement.dto.CreateProductDTO;
import com.ordermanagement.ordermanagement.model.ProductModel;
import com.ordermanagement.ordermanagement.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductModel> list(@RequestParam(required = false) Boolean active, @RequestParam(required = false) String category) {
        return productService.findAll(active, category);
    }

    @PostMapping
    public ResponseEntity<ProductModel> createProduct(@RequestBody @Valid CreateProductDTO data) {
        ProductModel createdProduct = productService.createProduct(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
}
