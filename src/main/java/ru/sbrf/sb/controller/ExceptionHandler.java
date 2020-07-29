package ru.sbrf.sb.controller;

import org.keycloak.common.VerificationException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(VerificationException.class)
    public String error(VerificationException e) {
        return e.getMessage();
    }
}
