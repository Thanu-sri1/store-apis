package com.codewithmosh.store.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private String checkoutUrl;
}
