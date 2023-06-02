package com.example.workwave.repositories;

import com.example.workwave.entities.products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface  productsRepository extends JpaRepository <products, Long> {
    @Query("SELECT p.price FROM products p WHERE p.order.o_id = :orderId")
    Integer getProductPricesByOrderId(@Param("orderId") Long orderId);


}