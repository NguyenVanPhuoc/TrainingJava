package com.example.lesson3.controller;

import com.example.lesson3.model.Product;
import com.example.lesson3.service.CrawlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/crawl")
public class CrawlController {

    private final CrawlService crawlService;

    public CrawlController(CrawlService crawlService) {
        this.crawlService = crawlService;
    }

    @PostMapping("/store")
    public ResponseEntity<?> crawl(@RequestBody Map<String, String> body) {
        try {
            Long storeId = Long.valueOf(body.get("storeId"));
            String url = body.get("url");
            List<Product> products = crawlService.crawlGrabFoodMenu(storeId, url);
            
            // Trả về response với thông tin chi tiết
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Crawl dữ liệu thành công");
            response.put("productsCount", products.size());
            System.out.println("response: " + response);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            System.out.println("[CrawlController] IOException: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to fetch source page: " + e.getMessage());
            return ResponseEntity.status(502).body(errorResponse);
        } catch (Exception e) {
            System.out.println("[CrawlController] Exception: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}