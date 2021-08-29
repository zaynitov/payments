package com.example.payments.repository;

import com.example.payments.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.math.BigDecimal;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Client c WHERE c.username = ?1")
    Client findByUsernameWithLock(String username);

    @Modifying
    @Query("update Client c set c.account = :account where c.username = :username")
    void payment(BigDecimal account, String username);
}
