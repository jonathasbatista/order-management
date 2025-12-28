package com.ordermanagement.ordermanagement.repository;

import com.ordermanagement.ordermanagement.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByActive(boolean active);
    List<ProductModel> findByCategory(String category);
    List<ProductModel> findByActiveAndCategory(boolean active, String category);
}
