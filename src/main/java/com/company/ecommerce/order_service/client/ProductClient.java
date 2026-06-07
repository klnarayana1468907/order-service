package com.company.ecommerce.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.ecommerce.order_service.dto.ProductResponse;

@FeignClient(name = "product-service",url = "http://localhost:8083")
public interface ProductClient {
	
	 @GetMapping("/api/products/{id}")
	 ProductResponse getProductById(@PathVariable Long id);
	 
	 @PutMapping("/api/products/{id}/reduce-stock")
	 void reduceStock(@PathVariable Long id, @RequestParam int quantity);

	 
}
