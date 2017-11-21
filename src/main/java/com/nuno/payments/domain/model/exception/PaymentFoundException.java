package com.nuno.payments.domain.model.exception;

public class PaymentFoundException extends InvalidActionException {
    public PaymentFoundException(String id) {
        super(String.format("Payment with ID %s already exists in the system", id));
    }
}
