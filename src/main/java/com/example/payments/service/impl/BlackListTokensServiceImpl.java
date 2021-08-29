package com.example.payments.service.impl;

import com.example.payments.service.BlackListTokensService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class BlackListTokensServiceImpl implements BlackListTokensService {
    private final LoadingCache<String, Integer> tokens = CacheBuilder.newBuilder()
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                public Integer load(String key) {
                    return 1;
                }
            });

    public void addToken(String token) {
        tokens.put(token, 1);
    }

    public boolean isForbidden(String token) {
        Integer integer = null;
        try {
            integer = tokens.get(token);
        } catch (ExecutionException e) {
            return false;
        }
        return integer == null;
    }


}
