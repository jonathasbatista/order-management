package com.ordermanagement.ordermanagement.service;

import com.ordermanagement.ordermanagement.model.ProductModel;
import com.ordermanagement.ordermanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductModel> findAll(Boolean active, String category) {

        if (active != null && category != null) {
            return productRepository.findByActiveAndCategory(active, category);
        }
        if (active != null) {
            return productRepository.findByActive(active);
        }
        if (category != null) {
            return productRepository.findByCategory(category);
        }
        return productRepository.findAll();
    }
}
