package ru.sbrf.sb.service;

import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;

public interface KeycloakTokenService {
    AccessToken extractAccessTokenFrom(String token) throws VerificationException;

    AccessToken authenticateFromAuthHeader(String token) throws VerificationException;
}
