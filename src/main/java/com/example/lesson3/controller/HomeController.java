package com.example.lesson3.controller;

import java.security.Principal;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.lesson3.model.Order;
import com.example.lesson3.model.Product;
import com.example.lesson3.model.Store;
import com.example.lesson3.model.User;
import com.example.lesson3.request.OrderRequest;
import com.example.lesson3.request.OrderedProductDTO;
import com.example.lesson3.service.ProductService;
import com.example.lesson3.service.StoreService;
import com.example.lesson3.service.UserService;
import com.example.lesson3.service.OrderService;

@Controller
public class HomeController {

	@Autowired
	private final StoreService storeService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	public HomeController(StoreService storeService) {
		this.storeService = storeService;
	}

	@GetMapping("/")
	public String home(Model model) {
		List<Store> stores = storeService.getAllStores();
		model.addAttribute("stores", stores);
		return "home";
	}

	@GetMapping("/store/detail/{storeId}")
	public String storeDetail(@PathVariable("storeId") Long storeId, Model model) {
		Store store = storeService.findById(storeId).orElse(null);
		model.addAttribute("store", store);

		List<Product> products = productService.findByStoreId(storeId);

		List<OrderedProductDTO> orderedProducts = orderService.findOrderedProductsToday(storeId);

		model.addAttribute("store", store);
		model.addAttribute("products", products);
		model.addAttribute("orderedProducts", orderedProducts);

		return "store_detail";
	}

	@PostMapping("/order/create")
	@ResponseBody
	public ResponseEntity<?> createOrder(@RequestBody OrderRequest request, Principal principal) {
		try {
			User user = userService.findByEmail(principal.getName());
			orderService.createOrder(user, request);
			List<OrderedProductDTO> orderedProducts = orderService.findOrderedProductsToday(request.getStoreId());

			return ResponseEntity.ok(orderedProducts);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi đặt hàng");
		}
	}
	
	@PostMapping("/orderItem/delete/{id}")
	@ResponseBody
	public ResponseEntity<?> deleteOrderItem(@PathVariable Long id) {
		try {
			orderService.deleteOrderItem(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm");
		}
	}
	
	@GetMapping("/user/orders/unpaid")
	public String getUnpaidOrders(Model model, Principal principal) {
	    User user = userService.findByEmail(principal.getName());

	    List<Order> unpaidOrders = orderService.findUnpaidOrdersByUser(user.getId());

	    double totalPrice = unpaidOrders.stream()
	        .flatMap(order -> order.getItems().stream())
	        .mapToDouble(item -> item.getQuantity() * item.getPrice())
	        .sum();
	    
	    model.addAttribute("orders", unpaidOrders);
	    model.addAttribute("totalPrice", totalPrice);

	    return "unpaid_orders";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login_user";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		SecurityContextHolder.clearContext();
		session.invalidate();
		return "redirect:/login";
	}
}
