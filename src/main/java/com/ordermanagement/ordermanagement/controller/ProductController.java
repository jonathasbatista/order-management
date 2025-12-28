package com.ordermanagement.ordermanagement.controller;

import com.ordermanagement.ordermanagement.model.ProductModel;
import com.ordermanagement.ordermanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
