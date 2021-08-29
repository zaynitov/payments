package com.example.payments.service;

import com.example.payments.domain.Client;

import java.math.BigDecimal;
import java.util.List;

public interface ClientService {
    Client getClient(String username);

    void saveClient(Client client);

    List<Client> getAllClients();

    Client findByUsernameWithLock(String username);

    void payment(BigDecimal subtract, String username);
}
