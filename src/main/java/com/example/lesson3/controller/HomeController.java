package com.example.lesson3.controller;

import java.security.Principal;
import java.util.List;

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
		model.addAttribute("contentPage", "/WEB-INF/views/home.jsp");
		model.addAttribute("pageTitle", "Trang chủ");
		return "templates/main";
	}

	@GetMapping("/contact")
	public String contact(Model model) {
		model.addAttribute("contentPage", "/WEB-INF/views/contact.jsp");
		model.addAttribute("pageTitle", "Liên hệ");
		return "templates/main";
	}

	@GetMapping("/store/detail/{slug}")
	public String storeDetail(@PathVariable("slug") String slug, Model model) {
		Store store = storeService.findBySlug(slug).orElse(null);
		if (store == null) {
			return "error/404";
		}

		List<Product> products = productService.findByStoreId(store.getId());

		List<OrderedProductDTO> orderedProducts = orderService.findOrderedProductsToday(store.getId());

		model.addAttribute("store", store);
		model.addAttribute("products", products);
		model.addAttribute("orderedProducts", orderedProducts);

		model.addAttribute("contentPage", "/WEB-INF/views/store_detail.jsp");
		model.addAttribute("pageTitle", "Trang chi tiết cửa hàng");
		return "templates/main";
	}

	// API để frontend polling lấy danh sách ordered products
	@GetMapping("/orderItem/getByStore/{storeId}")
	@ResponseBody
	public ResponseEntity<?> getOrderedProductsByStore(@PathVariable Long storeId) {
		try {
			List<OrderedProductDTO> orderedProducts = orderService.findOrderedProductsToday(storeId);
			return ResponseEntity.ok(orderedProducts);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lấy danh sách đơn hàng");
		}
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
		model.addAttribute("contentPage", "/WEB-INF/views/unpaid_orders.jsp");
		model.addAttribute("pageTitle", "Trang hóa đơn chưa thanh toán");
		return "templates/main";
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
