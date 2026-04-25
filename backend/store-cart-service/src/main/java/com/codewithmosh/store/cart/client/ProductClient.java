package com.codewithmosh.store.cart.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP client to call the Product Service.
 * Fetches product price so CartItem can store it locally.
 */
@Component
@Slf4j
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(@Value("${services.product.url}") String productServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(productServiceUrl)
                .build();
    }

    /**
     * Returns the price of the product, or null if not found.
     */
    public BigDecimal getProductPrice(Long productId) {
        try {
            var response = restClient.get()
                    .uri("/products/{id}", productId)
                    .retrieve()
                    .body(ProductResponse.class);

            return response != null ? response.price() : null;
        } catch (Exception e) {
            log.error("Failed to fetch product {} from product-service: {}", productId, e.getMessage());
            return null;
        }
    }

    public boolean productExists(Long productId) {
        return getProductPrice(productId) != null;
    }

    /**
     * Internal record to deserialize the product-service response.
     */
    public record ProductResponse(Long id, String name, BigDecimal price, Byte categoryId) {}
}
