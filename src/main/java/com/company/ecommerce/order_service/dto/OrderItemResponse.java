package com.company.ecommerce.order_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
	
	private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private Double subtotal;

    // getters & setters

}
