package com.ordermanagement.ordermanagement.service;

import com.ordermanagement.ordermanagement.model.ProductModel;
import com.ordermanagement.ordermanagement.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldFilterByActiveAndCategory() {
        String category = "Electronics";
        Boolean isActive = true;
        List<ProductModel> mockProducts = List.of(new ProductModel());

        when(productRepository.findByActiveAndCategory(isActive, category))
                .thenReturn(mockProducts);

        List<ProductModel> result = productService.findAll(isActive, category);

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByActiveAndCategory(isActive, category);
    }

    @Test
    void shouldFilterByActiveOnly() {
        Boolean isActive = true;
        List<ProductModel> mockProducts = List.of(new ProductModel(), new ProductModel());

        when(productRepository.findByActive(isActive)).thenReturn(mockProducts);

        List<ProductModel> result = productService.findAll(isActive, null);

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findByActive(isActive);
    }

    @Test
    void shouldFilterByCategoryOnly() {
        String category = "Furniture";
        List<ProductModel> mockProducts = List.of(new ProductModel());

        when(productRepository.findByCategory(category)).thenReturn(mockProducts);

        List<ProductModel> result = productService.findAll(null, category);

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByCategory(category);
    }

    @Test
    void shouldFindAllWhenNoFilters() {
        when(productRepository.findAll()).thenReturn(List.of(new ProductModel()));

        List<ProductModel> result = productService.findAll(null, null);

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }
}