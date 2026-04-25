package com.codewithmosh.store.order.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GetOrderItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
}
