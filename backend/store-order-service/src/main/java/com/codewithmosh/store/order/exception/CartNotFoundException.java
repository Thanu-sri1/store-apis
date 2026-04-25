package com.codewithmosh.store.order.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() { super("Cart not found"); }
}
