package com.example.payments.controller;

import com.example.payments.domain.Client;
import com.example.payments.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
//todo controller for testing(remove!)
public class ClientController {
    private final ClientService clientService;
//all api for test

    @GetMapping("/createadmin")
    public void createAdmin() {
        Client client = new Client();
        client.setUsername("admin");
        client.setPassword("admin");
        client.setRoles("ADMIN");
        clientService.saveClient(client);
    }

    @GetMapping("/showusers")
    public List<Client> showUsers() {
        return clientService.getAllClients();
    }
}
