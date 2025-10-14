package com.example.lesson3.repository;

import com.example.lesson3.model.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void testQueries() {
        Store s1 = new Store(); s1.setName("Alpha Coffee"); s1.setSlug("x3"); s1.setAddress("1 A St"); s1.setStatus(1);
        Store s2 = new Store(); s2.setName("Beta Tea"); s2.setSlug("x4"); s2.setAddress("2 B St"); s2.setStatus(0);
        storeRepository.save(s1); storeRepository.save(s2);

        assertFalse(storeRepository.findByStatus(1).isEmpty());

        Page<Store> byName = storeRepository.findByNameContainingIgnoreCase("cof", PageRequest.of(0, 10));
        assertTrue(byName.getContent().stream().anyMatch(s -> s.getName().toLowerCase().contains("cof")));

        Page<Store> byNameAndStatus = storeRepository.findByNameContainingIgnoreCaseAndStatus("a", 1, PageRequest.of(0, 10));
        assertTrue(byNameAndStatus.getContent().stream().allMatch(s -> s.getStatus() == 1));

        Page<Store> byStatus = storeRepository.findByStatus(0, PageRequest.of(0, 10));
        assertTrue(byStatus.getContent().stream().allMatch(s -> s.getStatus() == 0));
    }
}
