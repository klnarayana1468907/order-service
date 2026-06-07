package com.company.ecommerce.order_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

	@NotNull
    private Long productId;

    @NotNull
    private String productName;

    @Positive
    private Double price;

    @Positive
    private Integer quantity;

}
