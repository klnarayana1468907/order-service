package com.company.ecommerce.order_service.dto;

import lombok.Data;

@Data
public class PaymentResponse {
	
    private Long paymentId;
    private String status;
    
}
