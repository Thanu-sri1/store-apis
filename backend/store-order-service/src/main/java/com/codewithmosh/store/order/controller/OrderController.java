package com.codewithmosh.store.order.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.codewithmosh.store.order.dto.GetOrderDto;
import com.codewithmosh.store.order.exception.OrderNotFoundException;
import com.codewithmosh.store.order.service.OrderService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<GetOrderDto> getAllOrders(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader
    ) {
        Long userId = parseLong(userIdHeader);
        return orderService.getAllOrders(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetOrderDto> getOrderById(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader
    ) {
        Long userId = parseLong(userIdHeader);
        var dto = orderService.getOrderById(id, userId);
        return ResponseEntity.ok(dto);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFound() {
        return ResponseEntity.notFound().build();
    }

    private Long parseLong(String value) {
        try {
            return value != null ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
