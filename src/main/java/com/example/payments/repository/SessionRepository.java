package com.example.payments.repository;

import com.example.payments.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface SessionRepository extends JpaRepository<Session, String> {
    @Modifying
    @Query("update Session s set s.lastVisit = :date where s.sessionId = :sessionId")
    void updateLastVisit(String sessionId, Date date);
}
