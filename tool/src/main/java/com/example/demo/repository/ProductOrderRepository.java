package com.example.demo.repository;

import com.example.demo.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    ProductOrder findByOrderNumber(String orderNumber);
}
