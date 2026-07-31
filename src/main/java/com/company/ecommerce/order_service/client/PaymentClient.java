package com.company.ecommerce.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.ecommerce.order_service.dto.PaymentRequest;
import com.company.ecommerce.order_service.dto.PaymentResponse;

@FeignClient(name = "payment-service")
public interface PaymentClient {
	
	@PostMapping("/api/payments")
    PaymentResponse makePayment(@RequestBody PaymentRequest request);

}
