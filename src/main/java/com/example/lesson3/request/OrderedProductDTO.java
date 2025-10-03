package com.example.lesson3.request;

public class OrderedProductDTO {
    private Long id;
    private String productName;
    private int quantity;
    private String customerName;
    private Long userId;
    private String note; 

    public OrderedProductDTO(Long id, String productName, int quantity, String customerName, Long userId, String note) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.customerName = customerName;
        this.userId = userId;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCustomerName() {
		return customerName;
	}

	public Long getUserId() {
        return userId;
    }

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
    
}

