package com.example.payments.service.impl;

import com.example.payments.domain.Client;
import com.example.payments.exception.BruteForceFraudException;
import com.example.payments.repository.ClientRepository;
import com.example.payments.service.BruteForceFraudDetectService;
import com.example.payments.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService, UserDetailsService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final BruteForceFraudDetectService bruteForceFraudDetectService;
    private final HttpServletRequest request;

    @Override
    public Client getClient(String username) {
        log.debug("Get client by username {}", username);
        return clientRepository.findByUsername(username);
    }

    @Override
    public void saveClient(Client client) {
        String password = client.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        client.setPassword(encodedPassword);
        clientRepository.save(client);
        log.debug("Client: {} was saved", client);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client findByUsernameWithLock(String username) {
        return clientRepository.findByUsernameWithLock(username);
    }

    @Override
    public void payment(BigDecimal subtract, String username) {
        clientRepository.payment(subtract, username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (bruteForceFraudDetectService.isFraud(getIP())) {
            log.error("Try fraud from ip: {}", request.getRemoteAddr());
            throw new BruteForceFraudException("Try fraud from ip: " + getIP());
        }
        Client client = clientRepository.findByUsername(username);
        if (client == null) {
            log.error("User was not founded with username {}", username);
            throw new UsernameNotFoundException("User was not founded with username " + username);
        } else {
            log.debug("User was founded with username {}", username);
        }

        List<SimpleGrantedAuthority> roles = Arrays.stream(client.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new User(client.getUsername(), client.getPassword(), roles);
    }

    private String getIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
