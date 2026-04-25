package com.codewithmosh.store.order.exception;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException() { super("Cart is empty"); }
}
