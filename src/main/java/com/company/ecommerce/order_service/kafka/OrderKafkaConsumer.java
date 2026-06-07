package com.company.ecommerce.order_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.company.ecommerce.common.event.PaymentFailedEvent;
import com.company.ecommerce.order_service.services.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderKafkaConsumer {
	
	private final OrderService orderService;
	
	@KafkaListener(
	        topics = "payment-failed",
	        groupId = "order-group"
	)
	public void consumePaymentFailed(PaymentFailedEvent event) {

	    System.out.println("Payment failed for order: " + event.getOrderId());

	    orderService.updateOrderStatus(
	            event.getOrderId(),
	            "CANCELLED"
	    );

	    System.out.println("Order cancelled successfully");
	}

}
