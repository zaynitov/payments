package com.example.payments.service;

import com.example.payments.exception.NoMoneyException;

public interface PaymentService {
    void payment(String username) throws NoMoneyException;
}
