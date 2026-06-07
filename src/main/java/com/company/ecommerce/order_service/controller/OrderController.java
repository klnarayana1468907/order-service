package com.company.ecommerce.order_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.ecommerce.order_service.dto.OrderRequest;
import com.company.ecommerce.order_service.dto.OrderResponse;
import com.company.ecommerce.order_service.service.impl.OrderServiceImpl;
import com.company.ecommerce.order_service.services.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor // 🔥 THIS IS KEY
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest request,
			@RequestHeader("X-User-Id") Long userId) {
		OrderResponse response = orderService.createOrder(request, userId);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/{orderId}/status")
	public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {

		orderService.updateOrderStatus(orderId, status);
		return ResponseEntity.ok("Order status updated");
	}

}
