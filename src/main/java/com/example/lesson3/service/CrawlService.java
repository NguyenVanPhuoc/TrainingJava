package com.example.lesson3.service;

import com.example.lesson3.model.Product;
import com.example.lesson3.model.Store;
import com.example.lesson3.repository.ProductRepository;
import com.example.lesson3.repository.StoreRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CrawlService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    // Constants for field limits
    private static final int MAX_NAME_LENGTH = 255;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final int MAX_IMAGE_URL_LENGTH = 500;

    public CrawlService(ProductRepository productRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    @Transactional
    public List<Product> crawlGrabFoodMenu(Long storeId, String inputUrlOrCode) throws IOException {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));

        // 1. XÓA CÁC SẢN PHẨM CŨ
        try {
            productRepository.deleteByStore_Id(storeId);
        } catch (Exception e) {
            System.out.println("[CrawlService] Lỗi khi xóa sản phẩm cũ: " + e.getMessage());
        }

        List<Product> products = new ArrayList<>();

        // 2) Thử parse HTML trước
        String fullUrl = normalizeToFullUrl(inputUrlOrCode);
        System.out.println("[CrawlService] Full URL: " + fullUrl);
        
        List<Product> htmlProducts = parseHtml(fullUrl, store);
        System.out.println("htmlProducts size: " + htmlProducts.size());
        
        if (!htmlProducts.isEmpty()) {
            products.addAll(htmlProducts);
        }

        // 3) Validate và chuẩn hóa dữ liệu trước khi lưu
        List<Product> validatedProducts = validateAndNormalizeProducts(products);
        
        System.out.println("[CrawlService] Validated products: " + validatedProducts.size());
        for (int i = 0; i < validatedProducts.size(); i++) {
            Product p = validatedProducts.get(i);
            System.out.println("Product " + i + ": " + p.getName() + " - " + p.getPrice());
        }

        try {
            List<Product> saved = productRepository.saveAll(validatedProducts);
            System.out.println("[CrawlService] Saved products: " + saved.size() + ", storeId=" + storeId);
            return saved;
        } catch (Exception e) {
            System.err.println("[CrawlService] Error saving products: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Could not save products to database", e);
        }
    }

    private List<Product> validateAndNormalizeProducts(List<Product> products) {
        List<Product> validated = new ArrayList<>();
        Set<String> seenNames = new HashSet<>();
        
        for (Product product : products) {
            try {
                Product validatedProduct = validateAndNormalizeProduct(product);
                if (validatedProduct != null) {
                    // Check duplicate by name
                    String nameKey = validatedProduct.getName().trim().toLowerCase();
                    if (!seenNames.contains(nameKey)) {
                        seenNames.add(nameKey);
                        validated.add(validatedProduct);
                    }
                }
            } catch (Exception e) {
                System.err.println("[CrawlService] Error validating product: " + e.getMessage());
            }
        }
        return validated;
    }

    private Product validateAndNormalizeProduct(Product product) {
        if (product == null) return null;
        
        // Validate và normalize name
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            System.out.println("[CrawlService] Skipping product with null/empty name");
            return null;
        }
        
        String name = product.getName().trim();
        if (name.length() > MAX_NAME_LENGTH) {
            name = name.substring(0, MAX_NAME_LENGTH);
            System.out.println("[CrawlService] Truncated name to: " + name);
        }
        product.setName(name);
        
        // Validate price
        if (product.getPrice() == null || product.getPrice() <= 0) {
            System.out.println("[CrawlService] Skipping product with invalid price: " + product.getName());
            return null;
        }
        
        // Validate và normalize description
        if (product.getDescription() != null) {
            String description = product.getDescription().trim();
            if (description.length() > MAX_DESCRIPTION_LENGTH) {
                description = description.substring(0, MAX_DESCRIPTION_LENGTH);
            }
            product.setDescription(description);
        } else {
            product.setDescription(""); // Set empty string instead of null
        }
        
        // Validate và normalize image URL
        if (product.getImage() != null) {
            String imageUrl = product.getImage().trim();
            if (imageUrl.length() > MAX_IMAGE_URL_LENGTH) {
                imageUrl = imageUrl.substring(0, MAX_IMAGE_URL_LENGTH);
            }
            product.setImage(imageUrl);
        } else {
            product.setImage(""); // Set empty string instead of null
        }
        
        // Validate store reference
        if (product.getStore() == null) {
            System.out.println("[CrawlService] Skipping product with null store: " + product.getName());
            return null;
        }
        
        return product;
    }

    private String normalizeToFullUrl(String input) {
        if (input == null) return null;
        if (input.startsWith("http")) return input;
        
        try {
            String decoded = URLDecoder.decode(input, StandardCharsets.UTF_8.name());
            if (decoded.startsWith("http")) return decoded;
        } catch (Exception e) {
            // Ignore decoding errors
        }
        
        return "https://food.grab.com/vn/vi/restaurant/" + input;
    }

    private List<Product> parseHtml(String url, Store store) {
        List<Product> products = new ArrayList<>();
        if (url == null || !url.startsWith("http")) return products;
        
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .referrer("https://food.grab.com/")
                    .timeout(30000)
                    .ignoreContentType(true)
                    .get();

            Elements productElements = doc.select("[data-selenium=menu-item], [data-testid=menu-item], [class*='menu-item']");
            
            if (productElements.isEmpty()) {
                productElements = doc.select("div:has(h3, h4):has(span:containsOwn(₫))");
            }
            
            if (productElements.isEmpty()) {
                productElements = doc.select("div[class*='item'], li[class*='item']");
            }
            
			
            for (Element el : productElements) {
				System.out.println(" elements " + el);
				try {
					String name = el.selectFirst("p[class*=itemNameTitle]") != null
							? el.selectFirst("p[class*=itemNameTitle]").text()
							: null;

					String priceText = el.selectFirst("p[class*=discountedPrice]") != null
							? el.selectFirst("p[class*=discountedPrice]").text()
							: null;

					String description = el.selectFirst("p[class*=itemDescription]") != null
							? el.selectFirst("p[class*=itemDescription]").text()
							: null;

					String image = el.selectFirst("img[class*=realImage]") != null
							? el.selectFirst("img[class*=realImage]").attr("src")
							: null;
                    System.out.println(" Anh san pham: " + image);

					if (name != null && priceText != null) {
						double price = parseVnCurrency(priceText);
						if (price > 0) {
							Product p = new Product();
							p.setName(name.trim());
							p.setPrice(price);
							p.setDescription(description.trim());
							p.setImage(image != null ? image : "");
							if (el.selectFirst("[class*=menuItem--disable]") != null) {
								p.setStatus(2); // Disable
							} else {
								p.setStatus(1); // Active
							}
							p.setStore(store);
							products.add(p);
						}
					}
				} catch (Exception e) {
					System.out.println("[CrawlService] Error parsing element: " + e.getMessage());
				}
			}
            
        } catch (Exception e) {
            System.out.println("[CrawlService] HTML parse failed: " + e.getMessage());
        }
        return products;
    }

    private double parseVnCurrency(String priceText) {
        if (priceText == null) return 0;
        
        // Bỏ các ký tự không phải số (kể cả ₫, khoảng trắng)
        String cleaned = priceText.replaceAll("[^0-9]", "");
        
        if (cleaned.isEmpty()) return 0;
        
        // Parse về double
        return Double.parseDouble(cleaned);
    }

}