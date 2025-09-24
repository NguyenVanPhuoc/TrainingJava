package com.example.lesson3.service;

import com.example.lesson3.constants.StoreStatus;
import com.example.lesson3.model.Store;
import com.example.lesson3.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStores() {
        List<Store> stores = Arrays.asList(new Store(), new Store());
        when(storeRepository.findByStatus(StoreStatus.ACTIVE)).thenReturn(stores);
        List<Store> result = storeService.getAllStores();
        assertEquals(2, result.size());
        verify(storeRepository).findByStatus(StoreStatus.ACTIVE);
    }

    @Test
    void testFindAllWithFilter_KeywordAndStatus() {
        Page<Store> page = new PageImpl<>(Collections.emptyList());
        when(storeRepository.findByNameContainingIgnoreCaseAndStatus(eq("coffee"), eq(1), any(Pageable.class)))
                .thenReturn(page);
        Page<Store> result = storeService.findAllWithFilter("coffee", 1, 1, 10);
        assertNotNull(result);
        verify(storeRepository).findByNameContainingIgnoreCaseAndStatus(eq("coffee"), eq(1), any(Pageable.class));
    }

    @Test
    void testFindAllWithFilter_KeywordOnly() {
        Page<Store> page = new PageImpl<>(Collections.emptyList());
        when(storeRepository.findByNameContainingIgnoreCase(eq("coffee"), any(Pageable.class)))
                .thenReturn(page);
        Page<Store> result = storeService.findAllWithFilter("coffee", null, 1, 10);
        assertNotNull(result);
        verify(storeRepository).findByNameContainingIgnoreCase(eq("coffee"), any(Pageable.class));
    }

    @Test
    void testFindAllWithFilter_StatusOnly() {
        Page<Store> page = new PageImpl<>(Collections.emptyList());
        when(storeRepository.findByStatus(eq(1), any(Pageable.class))).thenReturn(page);
        Page<Store> result = storeService.findAllWithFilter(null, 1, 1, 10);
        assertNotNull(result);
        verify(storeRepository).findByStatus(eq(1), any(Pageable.class));
    }

    @Test
    void testFindAllWithFilter_None() {
        Page<Store> page = new PageImpl<>(Collections.emptyList());
        when(storeRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<Store> result = storeService.findAllWithFilter(null, null, 1, 10);
        assertNotNull(result);
        verify(storeRepository).findAll(any(Pageable.class));
    }

    @Test
    void testFindById() {
        Store s = new Store(); s.setId(7L);
        when(storeRepository.findById(7L)).thenReturn(Optional.of(s));
        Optional<Store> result = storeService.findById(7L);
        assertTrue(result.isPresent());
        assertEquals(7L, result.get().getId());
    }

    @Test
    void testSaveStore() {
        Store s = new Store();
        when(storeRepository.save(s)).thenReturn(s);
        Store result = storeService.saveStore(s);
        assertNotNull(result);
        verify(storeRepository).save(s);
    }

    @Test
    void testDeleteStore_WhenExists() {
        Store s = new Store(); s.setId(9L);
        s.setImage("stores/logo.png");
        when(storeRepository.findById(9L)).thenReturn(Optional.of(s));

        storeService.deleteStore(9L);

        verify(storeRepository).deleteById(9L);
    }

    @Test
    void testDeleteStore_WhenNotExists() {
        when(storeRepository.findById(100L)).thenReturn(Optional.empty());
        storeService.deleteStore(100L);
        verify(storeRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteMultipleStores() {
        Store a = new Store(); a.setId(1L);
        Store b = new Store(); b.setId(2L);
        List<Store> stores = Arrays.asList(a, b);
        when(storeRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(stores);

        storeService.deleteMultipleStores(Arrays.asList(1L, 2L));

        verify(storeRepository).deleteAll(stores);
    }
}
