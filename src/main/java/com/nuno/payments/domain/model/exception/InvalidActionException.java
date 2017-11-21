package com.nuno.payments.domain.model.exception;

public abstract class InvalidActionException extends RuntimeException {
    public InvalidActionException(String message) {
        super(message);
    }
}
