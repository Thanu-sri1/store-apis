package com.codewithmosh.store.order.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codewithmosh.store.order.entities.Order;
import com.codewithmosh.store.order.entities.OrderItem;
import com.codewithmosh.store.order.entities.Status;
import com.codewithmosh.store.order.exception.PaymentException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;

@Service
public class StripePaymentService implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "checkout-failure")
                    .putMetadata("order_Id", order.getId().toString());

            order.getOrderItems().forEach(item -> builder.addLineItem(getLineItem(item)));

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());

        } catch (StripeException ex) {
            throw new PaymentException();
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        var signature = request.getHeader().get("stripe-signature");
        var payload = request.getPayload();

        try {
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            var orderId = extractOrderId(event);

            return switch (event.getType()) {
                case "checkout.session.completed", "payment_intent.succeeded", "charge.succeeded" ->
                        Optional.of(new PaymentResult(orderId, Status.PAID));
                case "payment_intent.payment_failed", "charge.failed" ->
                        Optional.of(new PaymentResult(orderId, Status.FAILED));
                default -> Optional.empty();
            };

        } catch (SignatureVerificationException e) {
            throw new PaymentException();
        }
    }

    private Long extractOrderId(Event event) {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow();
        String orderId = null;

        if (stripeObject instanceof Session session) {
            orderId = session.getMetadata().get("order_Id");
        } else if (stripeObject instanceof PaymentIntent paymentIntent) {
            orderId = paymentIntent.getMetadata().get("order_Id");
        } else if (stripeObject instanceof Charge charge) {
            try {
                var pi = PaymentIntent.retrieve(charge.getPaymentIntent());
                orderId = pi.getMetadata().get("order_Id");
            } catch (StripeException e) {
                throw new PaymentException();
            }
        }

        if (orderId == null) throw new PaymentException();
        return Long.valueOf(orderId);
    }

    private LineItem getLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity((long) item.getQuantity())
                .setPriceData(createPriceData(item))
                .build();
    }

    private PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(item.getProductName() != null ? item.getProductName() : "Product")
                                .build()
                )
                .build();
    }
}
