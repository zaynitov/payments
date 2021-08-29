package com.example.payments.security;

import com.example.payments.service.BlackListTokensService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvalidateTokenLogoutHandler implements LogoutHandler {
    private final BlackListTokensService blackListTokensService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.debug("Logout user");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if ((authorizationHeader != null) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());
            blackListTokensService.addToken(token);
        }
    }
}