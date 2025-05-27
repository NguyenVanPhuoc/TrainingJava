package com.example.lesson3.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.lesson3.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
} 