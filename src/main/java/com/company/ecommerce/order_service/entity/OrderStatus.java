package com.company.ecommerce.order_service.entity;

public enum OrderStatus {
	
	CREATED,     // order placed
    PAID,        // payment successful
    CANCELLED,   // user/admin cancelled
    SHIPPED,     // shipped
    DELIVERED,   // completed
    CONFIRMED,
    FAILED


}
