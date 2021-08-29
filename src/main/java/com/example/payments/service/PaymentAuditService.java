package com.example.payments.service;

import com.example.payments.domain.PaymentAudit;

import java.util.List;

public interface PaymentAuditService {
    void audit(PaymentAudit paymentAudit);
    List<PaymentAudit> showAll();
}
