package com.company.ecommerce.order_service.dto;

import com.company.ecommerce.order_service.entity.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {

	private Long orderId;
    private Double totalAmount;
    private OrderStatus status;
}
