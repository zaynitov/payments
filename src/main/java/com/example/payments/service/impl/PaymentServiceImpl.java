package com.example.payments.service.impl;

import com.example.payments.domain.Client;
import com.example.payments.domain.PaymentAudit;
import com.example.payments.exception.NoMoneyException;
import com.example.payments.service.ClientService;
import com.example.payments.service.PaymentAuditService;
import com.example.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final ClientService clientService;
    private final PaymentAuditService paymentAuditService;

    @Value("${payment.amount}")
    private String paymentAmount;

    @Override
    @Transactional
    public void payment(String username) throws NoMoneyException {
        Client client = clientService.findByUsernameWithLock(username);
        log.debug("Client: {} is starting to pay", client);
        if (client.getAccount().compareTo(new BigDecimal(paymentAmount)) > 0) {
            BigDecimal subtract = client.getAccount().subtract(new BigDecimal(paymentAmount));
            clientService.payment(subtract, username);
            log.debug("Client: {} is payed", client);
            PaymentAudit paymentAudit = new PaymentAudit();
            paymentAudit.setClient(client);
            paymentAudit.setTransAmount(paymentAmount);
            paymentAudit.setTransTime(new Date());
            paymentAuditService.audit(paymentAudit);
        } else {
            log.error("Client: {} has only: {} dollars. He needs more than {} dollars", client.getUsername(), client.getAccount(), paymentAmount);
            throw new NoMoneyException("You have only " + client.getAccount() + " dollars. You need more than " + paymentAmount + " dollars");
        }
    }
}
