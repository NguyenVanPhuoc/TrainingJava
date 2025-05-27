package com.example.lesson3.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.lesson3.model.Order;
import com.example.lesson3.model.OrderItem;
import com.example.lesson3.model.Product;
import com.example.lesson3.model.Store;
import com.example.lesson3.model.User;
import com.example.lesson3.repository.OrderRepository;
import com.example.lesson3.repository.ProductRepository;
import com.example.lesson3.repository.StoreRepository;
import com.example.lesson3.repository.OrderItemRepository;
import com.example.lesson3.request.OrderRequest;
import com.example.lesson3.request.OrderedProductDTO;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final StoreRepository storeRepository;
	private final OrderItemRepository orderItemRepository;

	public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
			StoreRepository storeRepository, OrderItemRepository orderItemRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.storeRepository = storeRepository;
		this.orderItemRepository = orderItemRepository;
	}
	
	public Page<Order> findAllWithFilter(String keyword, String status, Long userId, String startDate, String endDate, int page, int size) {
    	Sort sort = Sort.by("id").descending();
    	Pageable pageable = PageRequest.of(page - 1, size, sort);

    	LocalDateTime startDateTime = null;
    	LocalDateTime endDateTime = null;
    	
    	if (startDate != null && !startDate.isEmpty()) {
    		startDateTime = LocalDate.parse(startDate).atStartOfDay();
    	}
    	if (endDate != null && !endDate.isEmpty()) {
    		endDateTime = LocalDate.parse(endDate).atTime(LocalTime.MAX);
    	}

    	if (keyword != null && !keyword.isEmpty() && status != null && !status.isEmpty() && userId != null) {
    	    return orderRepository.searchByStoreNameAndStatusAndUserId(keyword, status, userId, pageable);
    	} else if (keyword != null && !keyword.isEmpty() && status != null && !status.isEmpty()) {
    	    return orderRepository.searchByStoreNameAndStatus(keyword, status, pageable);
    	} else if (keyword != null && !keyword.isEmpty() && userId != null) {
    	    return orderRepository.searchByStoreNameAndUserId(keyword, userId, pageable);
    	} else if (status != null && !status.isEmpty() && userId != null) {
    	    return orderRepository.findByStatusAndUserId(status, userId, pageable);
    	} else if (keyword != null && !keyword.isEmpty()) {
    	    return orderRepository.searchByStoreName(keyword, pageable);
    	} else if (status != null && !status.isEmpty()) {
            return orderRepository.findByStatus(status, pageable);
    	} else if (userId != null) {
            return orderRepository.findByUserId(userId, pageable);
    	} else if (startDateTime != null && endDateTime != null) {
            return orderRepository.findByCreatedAtBetween(startDateTime, endDateTime, pageable);
    	} else if (startDateTime != null) {
            return orderRepository.findByCreatedAtGreaterThanEqual(startDateTime, pageable);
    	} else if (endDateTime != null) {
            return orderRepository.findByCreatedAtLessThanEqual(endDateTime, pageable);
    	} else {
            return orderRepository.findAll(pageable);
        }
    }

	public void createOrder(User user, OrderRequest request) {
		System.out.println("request: " + request);
		if (request == null || request.getStoreId() == null || request.getItems() == null
				|| request.getItems().isEmpty()) {
			throw new RuntimeException("Invalid order request");
		}

		Order order = new Order();
		order.setUser(user);
		System.out.println("User found: " + user);

		// Kiểm tra tồn tại của cửa hàng
		Store store = storeRepository.findById(request.getStoreId()).orElseThrow(() -> {
			System.out.println("Store not found for ID: " + request.getStoreId());
			return new RuntimeException("Store not found");
		});
		order.setStore(store);
		System.out.println("Store set: " + store);

		order.setTotalPrice(request.getTotal());
		order.setStatus("unpaid");
		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());

		List<OrderItem> orderItems = new ArrayList<>();

		// Kiểm tra các items trong đơn hàng
		for (OrderRequest.OrderItemRequest itemReq : request.getItems()) {
			if (itemReq.getProductId() == null || itemReq.getQuantity() <= 0 || itemReq.getPrice() <= 0) {
				throw new RuntimeException("Invalid order item: " + itemReq);
			}

			Product product = productRepository.findById(itemReq.getProductId()).orElseThrow(() -> {
				System.out.println("Product not found for ID: " + itemReq.getProductId());
				return new RuntimeException("Product not found");
			});

			OrderItem item = new OrderItem();
			item.setOrder(order);
			item.setProduct(product);
			item.setQuantity(itemReq.getQuantity());
			item.setPrice(itemReq.getPrice());
			item.setNote(itemReq.getNote());
			item.setCreatedAt(LocalDateTime.now());
			item.setUpdatedAt(LocalDateTime.now());
			orderItems.add(item);
		}

		order.setOrderItems(orderItems);
		orderRepository.save(order);
	}

	public List<OrderedProductDTO> findOrderedProductsToday(Long storeId) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

		List<Order> orders = orderRepository.findByStoreIdAndCreatedAtBetween(storeId, startOfDay, endOfDay);

		List<OrderedProductDTO> result = new ArrayList<>();

		for (Order order : orders) {
			String customerName = order.getUser().getName();

			for (OrderItem item : order.getOrderItems()) {
				OrderedProductDTO dto = new OrderedProductDTO(item.getId(), item.getProduct().getName(), item.getQuantity(),
						customerName, item.getNote());
				result.add(dto);
			}
		}

		return result;
	}
	
	public List<Order> findUnpaidOrdersByUser(Long userId) {
	    return orderRepository.findByUserIdAndStatus(userId, "unpaid");
	}

	public void updateOrderStatus(Long orderId, String status) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		order.setStatus(status);
		order.setUpdatedAt(LocalDateTime.now());
		orderRepository.save(order);
	}

	public void deleteOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		orderRepository.delete(order);
	}
	
	public void deleteMultipleOrders(List<Long> orderIds) {
		List<Order> orders = orderRepository.findAllById(orderIds);
		orderRepository.deleteAll(orders);
	}

	public Order getOrderById(Long id) {
		return orderRepository.findById(id).orElse(null);
	}

	public void deleteOrderItem(Long orderItemId) {
		OrderItem orderItem = orderItemRepository.findById(orderItemId)
				.orElseThrow(() -> new RuntimeException("Order item not found"));
		
		Order order = orderItem.getOrder();
		
		// Xóa OrderItem khỏi database
		orderItemRepository.delete(orderItem);
		
		// Kiểm tra xem Order còn OrderItem nào không
		List<OrderItem> remainingItems = orderItemRepository.findByOrderId(order.getId());
		
		if (remainingItems.isEmpty()) {
			// Nếu không còn OrderItem nào, xóa luôn Order
			orderRepository.delete(order);
		} else {
			// Nếu còn OrderItem, cập nhật lại tổng tiền của Order
			double newTotal = remainingItems.stream()
					.mapToDouble(item -> item.getPrice() * item.getQuantity())
					.sum();
			order.setTotalPrice(newTotal);
			orderRepository.save(order);
		}
	}

}
