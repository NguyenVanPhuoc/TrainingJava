package com.example.lesson3.repository;

import com.example.lesson3.model.Order;
import com.example.lesson3.model.Store;
import com.example.lesson3.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired private OrderRepository orderRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void testStatusAndUserQueries() {
        User user = new User(); user.setEmail("o@example.com"); user.setUsername("o"); user.setPassword("p"); user.setRole(1);
        user = userRepository.save(user);
        Store store = new Store(); store.setName("Gamma Cafe");  store.setSlug("x1"); store.setAddress("9 Gamma Rd"); store.setStatus(1); store = storeRepository.save(store);

        Order o1 = new Order(); o1.setUser(user); o1.setStore(store); o1.setStatus("paid"); o1.setCreatedAt(LocalDateTime.now());
        Order o2 = new Order(); o2.setUser(user); o2.setStore(store); o2.setStatus("unpaid"); o2.setCreatedAt(LocalDateTime.now());
        orderRepository.save(o1); orderRepository.save(o2);

        Page<Order> byStatus = orderRepository.findByStatus("paid", PageRequest.of(0, 10));
        assertTrue(byStatus.getContent().stream().allMatch(o -> "paid".equals(o.getStatus())));

        Page<Order> byUser = orderRepository.findByUserId(user.getId(), PageRequest.of(0, 10));
        assertTrue(byUser.getTotalElements() >= 2);

        Page<Order> byStatusAndUser = orderRepository.findByStatusAndUserId("unpaid", user.getId(), PageRequest.of(0, 10));
        assertTrue(byStatusAndUser.getContent().stream().allMatch(o -> "unpaid".equals(o.getStatus())));
    }
}
