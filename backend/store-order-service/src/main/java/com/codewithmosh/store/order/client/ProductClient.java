package com.codewithmosh.store.order.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(@Value("${services.product.url:http://localhost:8083}") String productServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(productServiceUrl)
                .build();
    }

    public ProductResponse getProduct(Long productId) {
        try {
            return restClient.get()
                    .uri("/products/{id}", productId)
                    .retrieve()
                    .body(ProductResponse.class);
        } catch (Exception e) {
            log.error("Failed to fetch product {} from product-service: {}", productId, e.getMessage());
            return null;
        }
    }

    public record ProductResponse(Long id, String name, BigDecimal price, Byte categoryId) {}
}
