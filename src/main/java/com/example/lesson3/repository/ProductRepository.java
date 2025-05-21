package com.example.lesson3.repository;

import com.example.lesson3.model.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findByStore_Id(Long storeId, Pageable pageable);
	Page<Product> findByStore_IdAndStatus(Long storeId, Integer status, Pageable pageable);
	Page<Product> findByStore_IdAndNameContainingIgnoreCase(Long storeId, String keyword, Pageable pageable);
	Page<Product> findByStore_IdAndNameContainingIgnoreCaseAndStatus(Long storeId, String keyword, Integer status, Pageable pageable);
	List<Product> findByStore_Id(Long storeId);
	List<Product> findByStore_IdOrderByStatusAscCreatedAtAsc(Long storeId);
}

