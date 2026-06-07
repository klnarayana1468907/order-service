package com.company.ecommerce.order_service.services;

import com.company.ecommerce.order_service.dto.OrderRequest;
import com.company.ecommerce.order_service.dto.OrderResponse;

public interface OrderService {
	
    OrderResponse createOrder(OrderRequest request, Long userId);
    
    void updateOrderStatus(Long orderId, String status);
    


}
