package com.example.payments.controller;

import com.example.payments.domain.PaymentAudit;
import com.example.payments.exception.BruteForceFraudException;
import com.example.payments.exception.NoMoneyException;
import com.example.payments.service.PaymentAuditService;
import com.example.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentControllerController {
    private final PaymentService paymentService;
    private final PaymentAuditService paymentAuditService;

    @GetMapping("/payment")
    public String payment() throws NoMoneyException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        paymentService.payment(username);
        log.debug("Payment happened by user: {}", username);
        return "payment";
    }

    @GetMapping("/showtrans")
    public List<PaymentAudit> showUsers() {
        return paymentAuditService.showAll();
    }

    @ControllerAdvice
    public static class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
        @ExceptionHandler(value = {NoMoneyException.class, BruteForceFraudException.class, InternalAuthenticationServiceException.class})
        protected ResponseEntity<String> handleBusinessExceptions(Exception ex, WebRequest request) {
            if ((ex instanceof NoMoneyException)) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}