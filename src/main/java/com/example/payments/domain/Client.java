package com.example.payments.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@ToString(exclude = "password")
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String username;
    private String password;
    private String roles;
    private BigDecimal account = BigDecimal.valueOf(8);
    @OneToMany
    private Set<Session> sessions;
    @OneToMany
    private Set<PaymentAudit> paymentAudits;
}
