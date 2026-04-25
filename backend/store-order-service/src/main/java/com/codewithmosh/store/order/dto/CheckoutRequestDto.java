package com.codewithmosh.store.order.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CheckoutRequestDto {
    private UUID cartId;
}
