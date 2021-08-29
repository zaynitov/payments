package com.example.payments.exception;

public class BruteForceFraudException extends RuntimeException {
    public BruteForceFraudException(String message) {
        super(message);
    }
}
