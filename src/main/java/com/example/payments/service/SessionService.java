package com.example.payments.service;

import com.example.payments.domain.Session;

import java.util.Date;
import java.util.List;

public interface SessionService {
    void save(Session session);
    List<Session> showAll();

    void updateLastVisit(String sessionId, Date date);
}
