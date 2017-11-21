package com.nuno.payments.domain.model.exception;

public class PaymentNotFoundException extends ResourceNotFoundException {
    public PaymentNotFoundException(String id) {
        super(String.format("Payment with ID %s not found", id));
    }
}
