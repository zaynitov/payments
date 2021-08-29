package com.example.payments.service.impl;

import com.example.payments.domain.PaymentAudit;
import com.example.payments.repository.PaymentAuditRepository;
import com.example.payments.service.PaymentAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentAuditServiceImpl implements PaymentAuditService {
    private final PaymentAuditRepository paymentAuditRepository;

    @Override
    public void audit(PaymentAudit paymentAudit) {
        paymentAuditRepository.save(paymentAudit);
    }

    @Override
    public List<PaymentAudit> showAll() {
        return paymentAuditRepository.findAll();
    }
}
