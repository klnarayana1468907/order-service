package com.company.ecommerce.order_service.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.ecommerce.common.event.OrderCreatedEvent;
import com.company.ecommerce.order_service.client.ProductClient;
import com.company.ecommerce.order_service.dto.OrderItemRequest;
import com.company.ecommerce.order_service.dto.OrderRequest;
import com.company.ecommerce.order_service.dto.OrderResponse;
import com.company.ecommerce.order_service.dto.ProductResponse;
import com.company.ecommerce.order_service.entity.Order;
import com.company.ecommerce.order_service.entity.OrderItem;
import com.company.ecommerce.order_service.entity.OrderStatus;
import com.company.ecommerce.order_service.kafka.OrderKafkaProducer;
import com.company.ecommerce.order_service.repository.OrderItemRepository;
import com.company.ecommerce.order_service.repository.OrderRepository;
import com.company.ecommerce.order_service.services.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;
    private final OrderKafkaProducer kafkaProducer;

    @Override
    public OrderResponse createOrder(OrderRequest request, Long userId) {

        double totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        // =========================
        // 1️⃣ Validate products & Reduce stock
        // =========================
        for (OrderItemRequest itemReq : request.getItems()) {

            // Get product from Product Service
            ProductResponse product =
                    productClient.getProductById(itemReq.getProductId());

            if (product == null) {
                throw new RuntimeException("Product not found: " 
                        + itemReq.getProductId());
            }

            if (product.getStock() < itemReq.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName());
            }

            // Reduce stock in Product Service
            productClient.reduceStock(
                    itemReq.getProductId(),
                    itemReq.getQuantity()
            );

            // Calculate subtotal
            double subtotal = itemReq.getPrice() * itemReq.getQuantity();
            totalAmount += subtotal;

            // Prepare order item
            OrderItem item = new OrderItem();
            item.setProductId(itemReq.getProductId());
            item.setProductName(product.getName()); // safer than request
            item.setPrice(itemReq.getPrice());
            item.setQuantity(itemReq.getQuantity());
            item.setSubtotal(subtotal);

            orderItems.add(item);
        }

        // =========================
        // 2️⃣ Create & Save Order
        // =========================
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED); // ✅ Confirm after stock success
        order.setTotalAmount(totalAmount);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // =========================
        // 3️⃣ Save Order Items
        // =========================
        for (OrderItem item : orderItems) {
        	
            item.setOrderId(savedOrder.getId());

        	 // Publish Kafka event
            OrderCreatedEvent event = new OrderCreatedEvent();
            
            event.setOrderId(savedOrder.getId());
            event.setUserId(userId);
            event.setAmount(totalAmount);
            event.setProductId(item.getProductId());
            event.setQuantity(item.getQuantity());
            
            kafkaProducer.publishOrderCreated(event);
        }

        orderItemRepository.saveAll(orderItems);
        
    
        // =========================
        // 4️⃣ Prepare Response
        // =========================
        OrderResponse response = new OrderResponse();
        response.setOrderId(savedOrder.getId());
        response.setTotalAmount(savedOrder.getTotalAmount());
        response.setStatus(savedOrder.getStatus());

        return response;
    }
    
    public void updateOrderStatusToPaid(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }
    
    @Override
    public void updateOrderStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Convert String → Enum
        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());

        order.setStatus(newStatus);
        orderRepository.save(order);
    }

}
