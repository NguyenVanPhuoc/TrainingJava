package com.example.lesson3.service;

import com.example.lesson3.model.Product;
import com.example.lesson3.repository.ProductRepository;
import com.example.lesson3.utils.FileUploadUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Page<Product> findAllWithFilter(Long storeId, String keyword, Integer status, int page, int size) {
		Sort sort = Sort.by("id").descending();
		Pageable pageable = PageRequest.of(page - 1, size, sort);

		if (keyword != null && !keyword.isEmpty() && status != null) {
			return productRepository.findByStore_IdAndNameContainingIgnoreCaseAndStatus(storeId, keyword, status,
					pageable);
		} else if (keyword != null && !keyword.isEmpty()) {
			return productRepository.findByStore_IdAndNameContainingIgnoreCase(storeId, keyword, pageable);
		} else if (status != null) {
			return productRepository.findByStore_IdAndStatus(storeId, status, pageable);
		} else {
			return productRepository.findByStore_Id(storeId, pageable);
		}
	}

	public Product save(Product product) {
		return productRepository.save(product);
	}

	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

	public List<Product> findByStoreId(Long storeId) {
		return productRepository.findByStore_IdOrderByStatusAscCreatedAtAsc(storeId);
	}

	public void deleteById(Long id) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isPresent()) {
			Product product = productOpt.get();
			if (product.getImage() != null) {
				String imagePath = product.getImage();
				if (imagePath.startsWith("products/")) {
					imagePath = imagePath.substring("products/".length());
				}
				try {
					FileUploadUtil.deleteFile("uploads/products", imagePath);
				} catch (IOException e) {
					System.err.println("Không thể xóa file ảnh: " + imagePath);
					e.printStackTrace();
				}
			}
			productRepository.deleteById(id);
		}
	}

	public List<Product> findByIds(List<Long> ids) {
		return productRepository.findAllById(ids);
	}

	public void deleteMultipleProducts(List<Long> ids) {
		List<Product> products = productRepository.findAllById(ids);
		System.out.println("Số lượng sản phẩm cần xóa: " + products.size());
		for (Product product : products) {
			System.out.println("Đang xử lý sản phẩm ID: " + product.getId());
			if (product.getImage() != null) {
				String imagePath = product.getImage();
				System.out.println("Đường dẫn ảnh gốc: " + imagePath);
				if (imagePath.startsWith("products/")) {
					imagePath = imagePath.substring("products/".length());
				}
				System.out.println("Đường dẫn ảnh sau khi xử lý: " + imagePath);
				try {
					FileUploadUtil.deleteFile("uploads/products", imagePath);
					System.out.println("Đã xóa file ảnh thành công: " + imagePath);
				} catch (IOException e) {
					System.err.println("Không thể xóa file ảnh: " + imagePath);
					e.printStackTrace();
				}
			} else {
				System.out.println("Sản phẩm không có ảnh");
			}
		}
		productRepository.deleteAll(products);
		System.out.println("Đã xóa " + products.size() + " sản phẩm khỏi database");
	}
}
