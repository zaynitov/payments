package com.example.payments.service.impl;

import com.example.payments.domain.Session;
import com.example.payments.repository.SessionRepository;
import com.example.payments.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

    @Override
    public void save(Session session) {
        sessionRepository.save(session);
    }

    @Override
    public List<Session> showAll() {
        return sessionRepository.findAll();
    }

    @Override
    @Transactional
    public void updateLastVisit(String sessionId, Date date) {
        sessionRepository.updateLastVisit(sessionId, date);
    }
}
