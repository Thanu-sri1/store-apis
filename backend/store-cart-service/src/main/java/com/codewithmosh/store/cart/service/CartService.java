package com.codewithmosh.store.cart.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codewithmosh.store.cart.client.ProductClient;
import com.codewithmosh.store.cart.dto.CartDto;
import com.codewithmosh.store.cart.dto.CartItemDto;
import com.codewithmosh.store.cart.entities.Cart;
import com.codewithmosh.store.cart.exception.CartItemNotFoundException;
import com.codewithmosh.store.cart.exception.CartNotFoundException;
import com.codewithmosh.store.cart.exception.ProductNotFoundException;
import com.codewithmosh.store.cart.mappers.CartMapper;
import com.codewithmosh.store.cart.repositories.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductClient productClient;

    public CartDto createCart() {
        var cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDto addCartItem(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

        // Fetch product price from product-service
        var price = productClient.getProductPrice(productId);
        if (price == null) {
            throw new ProductNotFoundException();
        }

        var cartItem = cart.getCartItem(productId);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = cart.addCartItem(productId, price);
        }

        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId, int quantity) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

        var cartItem = cart.getCartItem(productId);
        if (cartItem == null) {
            throw new CartItemNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto removeCartItem(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);

        var cartItem = cart.getCartItem(productId);
        if (cartItem != null) {
            cart.getItems().remove(cartItem);
        }

        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public void clearCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElseThrow(CartNotFoundException::new);
        cartRepository.deleteById(cartId);
    }
}
