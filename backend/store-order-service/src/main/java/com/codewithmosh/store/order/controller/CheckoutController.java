package com.codewithmosh.store.order.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codewithmosh.store.order.dto.CheckoutRequestDto;
import com.codewithmosh.store.order.dto.ErrorDto;
import com.codewithmosh.store.order.exception.CartEmptyException;
import com.codewithmosh.store.order.exception.CartNotFoundException;
import com.codewithmosh.store.order.exception.PaymentException;
import com.codewithmosh.store.order.exception.UnauthorizedUserException;
import com.codewithmosh.store.order.service.OrderService;
import com.codewithmosh.store.order.service.WebhookRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody CheckoutRequestDto request,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader
    ) {
        Long userId = parseLong(userIdHeader);
        var orderDto = orderService.createOrder(request, userId);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ) {
        orderService.handleWebhook(new WebhookRequest(headers, payload));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating checkout session"));
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedUserException ex) {
        return ResponseEntity.status(401).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<?> handleCartEmpty() {
        return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty"));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<?> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
    }

    private Long parseLong(String value) {
        try {
            return value != null ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
