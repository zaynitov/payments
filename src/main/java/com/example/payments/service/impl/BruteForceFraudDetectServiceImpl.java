package com.example.payments.service.impl;

import com.example.payments.service.BruteForceFraudDetectService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BruteForceFraudDetectServiceImpl implements BruteForceFraudDetectService {

    @Value("${max.attempts.login}")
    private Integer maxAttemptsLogin;

    private final LoadingCache<String, Integer> ipToAttemptsLogin = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                public Integer load(String key) {
                    return 0;
                }
            });

    public void failedAttemptIncrement(String ip) {
        try {
            Integer countOfAttempts = ipToAttemptsLogin.get(ip);
            if (countOfAttempts != null) {
                ipToAttemptsLogin.put(ip, countOfAttempts + 1);
            }
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
        }
    }

    public boolean isFraud(String ip) {
        Integer countOfAttempts;
        try {
            countOfAttempts = ipToAttemptsLogin.get(ip);
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return (countOfAttempts != null) && (countOfAttempts > maxAttemptsLogin);
    }
}
