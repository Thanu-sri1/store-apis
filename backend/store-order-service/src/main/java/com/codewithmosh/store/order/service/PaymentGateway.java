package com.codewithmosh.store.order.service;

import com.codewithmosh.store.order.entities.Order;

/**
 * Abstraction over payment gateway (currently Stripe).
 */
public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    java.util.Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
