package com.example.lesson3.service;

import com.example.lesson3.model.*;
import com.example.lesson3.repository.*;
import com.example.lesson3.request.OrderRequest;
import com.example.lesson3.request.OrderedProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @Mock private StoreRepository storeRepository;
    @Mock private OrderItemRepository orderItemRepository;

    @InjectMocks private OrderService orderService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllWithFilter_Combinations() {
        Page<Order> page = new PageImpl<>(Collections.emptyList());

        when(orderRepository.searchByStoreNameAndStatusAndUserId(eq("kiosk"), eq("paid"), eq(1L), any(Pageable.class))).thenReturn(page);
        assertNotNull(orderService.findAllWithFilter("kiosk", "paid", 1L, null, null, 1, 10));

        when(orderRepository.searchByStoreNameAndStatus(eq("kiosk"), eq("paid"), any(Pageable.class))).thenReturn(page);
        assertNotNull(orderService.findAllWithFilter("kiosk", "paid", null, null, null, 1, 10));

        when(orderRepository.searchByStoreNameAndUserId(eq("kiosk"), eq(1L), any(Pageable.class))).thenReturn(page);
        assertNotNull(orderService.findAllWithFilter("kiosk", null, 1L, null, null, 1, 10));

        when(orderRepository.findByStatusAndUserId(eq("paid"), eq(1L), any(Pageable.class))).thenReturn(page);
        assertNotNull(orderService.findAllWithFilter(null, "paid", 1L, null, null, 1, 10));

        when(orderRepository.searchByStoreName(eq("kiosk"), any(Pageable.class))).thenReturn(page);
        assertNotNull(orderService.findAllWithFilter("kiosk", null, null, null, null, 1, 10));

        when(orderRepository.findByStatus(eq("paid"), any(Pageable.class))).thenReturn(page);
        assertNotNull(orderService.findAllWithFilter(null, "paid", null, null, null, 1, 10));

        when(orderRepository.findByUserId(eq(1L), any(Pageable.class))).thenReturn(page);
        assertNotNull(orderService.findAllWithFilter(null, null, 1L, null, null, 1, 10));

        when(orderRepository.findByCreatedAtBetween(any(), any(), any(Pageable.class))).thenReturn(page);
        assertNotNull(orderService.findAllWithFilter(null, null, null, "2024-01-01", "2024-12-31", 1, 10));

        when(orderRepository.findByCreatedAtGreaterThanEqual(any(), any(Pageable.class))).thenReturn(page);
        assertNotNull(orderService.findAllWithFilter(null, null, null, "2024-01-01", null, 1, 10));

        when(orderRepository.findByCreatedAtLessThanEqual(any(), any(Pageable.class))).thenReturn(page);
        assertNotNull(orderService.findAllWithFilter(null, null, null, null, "2024-12-31", 1, 10));

        when(orderRepository.findAll(any(Pageable.class))).thenReturn(page);
        assertNotNull(orderService.findAllWithFilter(null, null, null, null, null, 1, 10));
    }

    @Test
    void testCreateOrder_Success() {
        User user = new User(); user.setId(1L); user.setName("Alice");
        OrderRequest req = new OrderRequest();
        req.setStoreId(5L);
        req.setTotal(100.0);
        List<OrderRequest.OrderItemRequest> items = new ArrayList<>();
        OrderRequest.OrderItemRequest item = new OrderRequest.OrderItemRequest();
        item.setProductId(7L);
        item.setQuantity(2);
        item.setPrice(10.0);
        item.setNote("no sugar");
        items.add(item);
        req.setItems(items);

        when(storeRepository.findById(5L)).thenReturn(Optional.of(new Store()));
        when(productRepository.findById(7L)).thenReturn(Optional.of(new Product()));

        orderService.createOrder(user, req);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testCreateOrder_InvalidRequestThrows() {
        assertThrows(RuntimeException.class, () -> orderService.createOrder(new User(), null));

        OrderRequest bad = new OrderRequest(); bad.setStoreId(1L); bad.setItems(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> orderService.createOrder(new User(), bad));
    }

    @Test
    void testFindOrderedProductsToday_Aggregates() {
        User u = new User(); u.setName("Bob");
        Product p = new Product(); p.setName("Juice");
        OrderItem oi = new OrderItem(); oi.setId(1L); oi.setProduct(p); oi.setQuantity(2); oi.setPrice(5.0); oi.setNote("less ice");
        Order order = new Order(); order.setUser(u); order.setOrderItems(Arrays.asList(oi));
        when(orderRepository.findByStoreIdAndCreatedAtBetween(anyLong(), any(), any())).thenReturn(Arrays.asList(order));

        List<OrderedProductDTO> result = orderService.findOrderedProductsToday(2L);
        assertEquals(1, result.size());
        assertEquals("Juice", result.get(0).getProductName());
        assertEquals("Bob", result.get(0).getCustomerName());
    }

    @Test
    void testFindUnpaidOrdersByUser() {
        when(orderRepository.findByUserIdAndStatus(1L, "unpaid")).thenReturn(Collections.emptyList());
        assertNotNull(orderService.findUnpaidOrdersByUser(1L));
    }

    @Test
    void testUpdateOrderStatus() {
        Order order = new Order(); order.setId(3L);
        when(orderRepository.findById(3L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        orderService.updateOrderStatus(3L, "paid");

        assertEquals("paid", order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void testDeleteOrder() {
        Order order = new Order();
        when(orderRepository.findById(4L)).thenReturn(Optional.of(order));
        orderService.deleteOrder(4L);
        verify(orderRepository).delete(order);
    }

    @Test
    void testDeleteMultipleOrders() {
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(orders);
        orderService.deleteMultipleOrders(Arrays.asList(1L, 2L));
        verify(orderRepository).deleteAll(orders);
    }

    @Test
    void testGetOrderById() {
        Order order = new Order(); order.setId(9L);
        when(orderRepository.findById(9L)).thenReturn(Optional.of(order));
        assertEquals(9L, orderService.getOrderById(9L).getId());
    }

    @Test
    void testDeleteOrderItem_DeletesOrderWhenLastItem() {
        Order order = new Order(); order.setId(1L);
        OrderItem item = new OrderItem(); item.setId(10L); item.setOrder(order); item.setPrice(5.0); item.setQuantity(1);
        when(orderItemRepository.findById(10L)).thenReturn(Optional.of(item));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Collections.emptyList());

        orderService.deleteOrderItem(10L);

        verify(orderRepository).delete(order);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testDeleteOrderItem_RecalculatesTotalWhenRemaining() {
        Order order = new Order(); order.setId(1L);
        OrderItem item = new OrderItem(); item.setId(10L); item.setOrder(order); item.setPrice(5.0); item.setQuantity(1);
        when(orderItemRepository.findById(10L)).thenReturn(Optional.of(item));
        OrderItem remaining = new OrderItem(); remaining.setPrice(3.0); remaining.setQuantity(2);
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Arrays.asList(remaining));

        orderService.deleteOrderItem(10L);

        assertEquals(6.0, order.getTotalPrice());
        verify(orderRepository).save(order);
    }
}
