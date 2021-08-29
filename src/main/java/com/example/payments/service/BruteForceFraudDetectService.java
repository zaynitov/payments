package com.example.payments.service;

public interface BruteForceFraudDetectService {

    public void failedAttemptIncrement(String ip);

    public boolean isFraud(String ip);
}
