package com.example.lesson3.repository;

import com.example.lesson3.model.Store;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
	List<Store> findByStatus(int status);
	Page<Store> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Store> findByNameContainingIgnoreCaseAndStatus(String keyword, Integer status, Pageable pageable);
    Page<Store> findByStatus(Integer status, Pageable pageable);
}
