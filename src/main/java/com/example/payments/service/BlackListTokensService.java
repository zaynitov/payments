package com.example.payments.service;

public interface BlackListTokensService {

    public void addToken(String token);

    public boolean isForbidden(String token);
}
