package com.example.lesson3.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.lesson3.model.Order;

// Chỉ cần thêm JpaSpecificationExecutor<Order>
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    
    // Tất cả các method hiện có giữ nguyên, không cần thay đổi
    List<Order> findByStoreIdAndCreatedAtBetween(Long storeId, LocalDateTime start, LocalDateTime end);
    List<Order> findByUserIdAndStatus(Long userId, String status);
    List<Order> findByStatus(int status);
    
    @Query("SELECT o FROM Order o WHERE LOWER(o.store.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Order> searchByStoreName(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE LOWER(o.store.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND o.status = :status")
    Page<Order> searchByStoreNameAndStatus(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);
    
    Page<Order> findByStatus(String status, Pageable pageable);

    Page<Order> findByUserId(Long userId, Pageable pageable);
    
    Page<Order> findByStatusAndUserId(String status, Long userId, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE LOWER(o.store.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND o.user.id = :userId")
    Page<Order> searchByStoreNameAndUserId(@Param("keyword") String keyword, @Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE LOWER(o.store.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND o.status = :status AND o.user.id = :userId")
    Page<Order> searchByStoreNameAndStatusAndUserId(@Param("keyword") String keyword, @Param("status") String status, @Param("userId") Long userId, Pageable pageable);
    
    Page<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    Page<Order> findByCreatedAtGreaterThanEqual(LocalDateTime startDate, Pageable pageable);
    
    Page<Order> findByCreatedAtLessThanEqual(LocalDateTime endDate, Pageable pageable);
}