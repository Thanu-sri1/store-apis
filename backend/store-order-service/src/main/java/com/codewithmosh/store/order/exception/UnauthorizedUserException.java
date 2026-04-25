package com.codewithmosh.store.order.exception;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException(String message) { super(message); }
}
