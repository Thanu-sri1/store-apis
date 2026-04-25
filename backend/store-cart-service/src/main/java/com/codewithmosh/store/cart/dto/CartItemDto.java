package com.codewithmosh.store.cart.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalPrice;
}
