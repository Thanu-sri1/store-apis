package com.codewithmosh.store.cart.exception;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException() {
        super("Cart item not found");
    }
}
