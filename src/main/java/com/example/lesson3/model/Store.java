package com.example.lesson3.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên cửa hàng là bắt buộc")
    private String name;
    
    @NotBlank(message = "Địa chỉ cửa hàng là bắt buộc")
    private String address;
    
    @Column(nullable = true)
    private String phone;

    // 1 = hoạt động, 0 = ngừng hoạt động
    private int status = 1;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Product> products;
    
    @Transient
    private MultipartFile imageFile;

    public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	private String image;
	
	private Double rating;
	
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "order_start_time")
	private LocalTime orderStartTime;

	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "order_end_time")
	private LocalTime orderEndTime;

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }
    
    public String getStatusName() {
	    switch (this.status) {
	        case 1:
	            return "Hoạt động";
	        case 2:
	            return "Ngừng hoạt động";
	        default:
	            return "UNKNOWN";
	    }
	}

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public LocalTime getOrderStartTime() {
	    return orderStartTime;
	}

	public void setOrderStartTime(LocalTime orderStartTime) {
	    this.orderStartTime = orderStartTime;
	}

	public LocalTime getOrderEndTime() {
	    return orderEndTime;
	}

	public void setOrderEndTime(LocalTime orderEndTime) {
	    this.orderEndTime = orderEndTime;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}
    
}
