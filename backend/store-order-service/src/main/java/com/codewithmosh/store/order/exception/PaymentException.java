package com.codewithmosh.store.order.exception;

public class PaymentException extends RuntimeException {
    public PaymentException() { super("Payment processing failed"); }
    public PaymentException(String message) { super(message); }
}
