package com.company.ecommerce.order_service.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

	    @NotEmpty
	    private List<OrderItemRequest> items;

    
    
}
