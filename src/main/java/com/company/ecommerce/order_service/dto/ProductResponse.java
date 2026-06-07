package com.company.ecommerce.order_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
	
	private Long id;
    private String name;
    private Double price;
    private Integer stock;


}
