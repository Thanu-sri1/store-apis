package com.codewithmosh.store.cart.dto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class CartDto {
    private UUID id;
    private BigDecimal price;
    private Set<CartItemDto> items;
}
