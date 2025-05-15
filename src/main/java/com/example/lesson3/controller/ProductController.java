package com.example.lesson3.controller;

import com.example.lesson3.model.Product;
import com.example.lesson3.service.ProductService;
import com.example.lesson3.utils.FileUploadUtil;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/stores/{storeId}/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(
            @PathVariable Long storeId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", required = false) Integer status,
            Model model) {

        Page<Product> productPage = productService.findAllWithFilter(storeId, keyword, status, page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("storeId", storeId);
        model.addAttribute("contentPage", "/WEB-INF/views/products/list.jsp");
        model.addAttribute("pageTitle", "Danh sách sản phẩm");
        model.addAttribute("currentPath", "/admin/stores/" + storeId + "/products");
        return "layouts/main";
    }

    @GetMapping("/create")
    public String createForm(@PathVariable Long storeId, Model model) {
        Product product = new Product();
        product.setStoreId(storeId);
        model.addAttribute("product", product);
        model.addAttribute("storeId", storeId);
        model.addAttribute("contentPage", "/WEB-INF/views/products/create.jsp");
        model.addAttribute("pageTitle", "Tạo mới sản phẩm");
        model.addAttribute("currentPath", "/admin/stores/" + storeId + "/products/create");
        return "layouts/main";
    }

    @PostMapping("/save")
    public String saveProduct(
            @PathVariable Long storeId,
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            if (product.getId() == null) {
                model.addAttribute("contentPage", "/WEB-INF/views/products/create.jsp");
                model.addAttribute("currentPath", "/admin/stores/" + storeId + "/products/create");
                model.addAttribute("pageTitle", "Tạo mới sản phẩm");
            } else {
                model.addAttribute("contentPage", "/WEB-INF/views/products/edit.jsp");
                model.addAttribute("currentPath", "/admin/stores/" + storeId + "/products/edit/" + product.getId());
                model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm");
            }
            model.addAttribute("storeId", storeId);
            return "layouts/main";
        }

        try {
        	MultipartFile image = product.getImageFile();
        	if (image != null && !image.isEmpty()) {
        	    if (product.getId() != null) {
        	        Optional<Product> existingProductOpt = productService.findById(product.getId());
        	        if (existingProductOpt.isPresent()) {
        	            Product existingProduct = existingProductOpt.get();
        	            if (existingProduct.getImage() != null) {
        	                String oldImageFilename = existingProduct.getImage();
        	                if (oldImageFilename.startsWith("products/")) {
        	                    oldImageFilename = oldImageFilename.substring("products/".length());
        	                }
        	                FileUploadUtil.deleteFile("uploads/products", oldImageFilename);
        	            }
        	        }
        	    }
        	    String filename = FileUploadUtil.saveFile("uploads/products", image);
        	    System.out.println("filename: " + filename);
        	    product.setImage("products/" + filename);
        	} else {
        	    if (product.getId() != null) {
        	        productService.findById(product.getId()).ifPresent(existingProduct -> {
        	            product.setImage(existingProduct.getImage());
        	        });
        	    }
        	}
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi khi xử lý ảnh.");
            return "redirect:/admin/stores/" + storeId + "/products";
        }

        product.setStoreId(storeId);
        productService.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm đã được lưu thành công!");
        return "redirect:/admin/stores/" + storeId + "/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long storeId, @PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        model.addAttribute("storeId", storeId);
        model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm");
        model.addAttribute("contentPage", "/WEB-INF/views/products/edit.jsp");
        model.addAttribute("currentPath", "/admin/stores/" + storeId + "/products/edit/" + id);
        return "layouts/main";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long storeId, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sản phẩm thành công!");
        return "redirect:/admin/stores/" + storeId + "/products";
    }

    @PostMapping("/delete-multiple")
    public String deleteMultipleProducts(
            @PathVariable Long storeId,
            @RequestParam("itemIds") String itemIds,
            RedirectAttributes redirectAttributes) {
        try {
            List<Long> ids = Arrays.stream(itemIds.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            
            productService.deleteMultipleProducts(ids);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa các sản phẩm đã chọn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa sản phẩm!");
        }
        return "redirect:/admin/stores/" + storeId + "/products";
    }
}

