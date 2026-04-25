package com.codewithmosh.store.order.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP client to call the Cart Service.
 * Fetches cart contents for checkout, then clears the cart.
 */
@Component
@Slf4j
public class CartClient {

    private final RestClient restClient;

    public CartClient(@Value("${services.cart.url}") String cartServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(cartServiceUrl)
                .build();
    }

    public CartResponse getCart(UUID cartId) {
        try {
            return restClient.get()
                    .uri("/cart/{id}", cartId)
                    .retrieve()
                    .body(CartResponse.class);
        } catch (Exception e) {
            log.error("Failed to fetch cart {}: {}", cartId, e.getMessage());
            return null;
        }
    }

    public void clearCart(UUID cartId) {
        try {
            restClient.delete()
                    .uri("/cart/{cartId}/items", cartId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Failed to clear cart {}: {}", cartId, e.getMessage());
        }
    }

    public record CartResponse(UUID id, BigDecimal price, List<CartItemResponse> items) {}

    public record CartItemResponse(Long id, Long productId, Integer quantity,
                                   BigDecimal totalPrice, String productName, BigDecimal unitPrice) {}
}
