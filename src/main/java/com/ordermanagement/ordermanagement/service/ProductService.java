package com.ordermanagement.ordermanagement.service;

import com.ordermanagement.ordermanagement.dto.CreateProductDTO;
import com.ordermanagement.ordermanagement.model.ProductModel;
import com.ordermanagement.ordermanagement.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public List<ProductModel> findAll(Boolean active, String category) {
        log.info("Buscando produtos. Filtros - Ativo: {}, Categoria: {}", active, category);

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

    @Transactional
    public ProductModel createProduct(CreateProductDTO dto) {
        log.info("Recebendo solicitação para criar novo produto: {}", dto.name());

        ProductModel product = new ProductModel();
        product.setName(dto.name());
        product.setPriceCents(dto.priceCents());
        product.setCategory(dto.category());
        product.setActive(true);

        ProductModel savedProduct = productRepository.save(product);

        log.info("Produto criado com sucesso. ID: {}", savedProduct.getId());

        return savedProduct;
    }
}