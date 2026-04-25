package com.codewithmosh.store.order.service;

import com.codewithmosh.store.order.entities.Status;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResult {
    private Long orderId;
    private Status paymentStatus;
}
