package com.codewithmosh.store.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codewithmosh.store.order.client.CartClient;
import com.codewithmosh.store.order.client.ProductClient;
import com.codewithmosh.store.order.dto.GetOrderDto;
import com.codewithmosh.store.order.dto.OrderDto;
import com.codewithmosh.store.order.dto.CheckoutRequestDto;
import com.codewithmosh.store.order.entities.Order;
import com.codewithmosh.store.order.entities.OrderItem;
import com.codewithmosh.store.order.entities.Status;
import com.codewithmosh.store.order.exception.CartEmptyException;
import com.codewithmosh.store.order.exception.CartNotFoundException;
import com.codewithmosh.store.order.exception.OrderNotFoundException;
import com.codewithmosh.store.order.exception.PaymentException;
import com.codewithmosh.store.order.exception.UnauthorizedUserException;
import com.codewithmosh.store.order.mappers.OrderMapper;
import com.codewithmosh.store.order.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final ProductClient productClient;
    private final PaymentGateway paymentGateway;
    private final OrderMapper orderMapper;

    /**
     * Create an order from cart contents, then initiate Stripe checkout.
     * userId comes from the X-User-Id header (injected by gateway).
     */
    public OrderDto createOrder(CheckoutRequestDto request, Long userId) {
        if (userId == null) {
            throw new UnauthorizedUserException("User not authenticated");
        }

        var cartId = request.getCartId();
        var cart = cartClient.getCart(cartId);

        if (cart == null) {
            throw new CartNotFoundException();
        }
        if (cart.items() == null || cart.items().isEmpty()) {
            throw new CartEmptyException();
        }

        // Build Order
        var order = Order.builder()
                .customerId(userId)
                .status(Status.PENDING)
                .totalPrice(cart.price())
                .build();

        // Map cart items → order items (snapshot price/name at order time)
        var orderItems = cart.items().stream().map(cartItem -> {
            var product = productClient.getProduct(cartItem.productId());
            var productName = product != null ? product.name() : "Product";

            return OrderItem.builder()
                    .order(order)
                    .productId(cartItem.productId())
                    .productName(productName)
                    .unitPrice(cartItem.unitPrice() != null ? cartItem.unitPrice()
                            : (cartItem.totalPrice() != null && cartItem.quantity() > 0
                                ? cartItem.totalPrice().divide(BigDecimal.valueOf(cartItem.quantity())) : BigDecimal.ZERO))
                    .quantity(cartItem.quantity())
                    .totalPrice(cartItem.totalPrice())
                    .build();
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        try {
            var session = paymentGateway.createCheckoutSession(order);
            cartClient.clearCart(cartId);
            return new OrderDto(order.getId(), session.getUrl());
        } catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;
        }
    }

    public List<GetOrderDto> getAllOrders(Long userId) {
        return orderRepository.findAllByCustomerId(userId).stream()
                .map(orderMapper::toGetOrderDto)
                .collect(Collectors.toList());
    }

    public GetOrderDto getOrderById(Long orderId, Long userId) {
        var order = orderRepository.findByIdAndCustomerId(orderId, userId)
                .orElseThrow(OrderNotFoundException::new);
        return orderMapper.toGetOrderDto(order);
    }

    public void handleWebhook(WebhookRequest request) {
        paymentGateway.parseWebhookRequest(request).ifPresent(paymentResult -> {
            var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
            order.setStatus(paymentResult.getPaymentStatus());
            orderRepository.save(order);
        });
    }
}
