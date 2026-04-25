package com.codewithmosh.store.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.codewithmosh.store.order.entities.Status;

import lombok.Data;

@Data
public class GetOrderDto {
    private Long id;
    private Status status;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;
    private List<GetOrderItemDto> orderItems;
}
