package ru.sbrf.sb.filter;

import lombok.RequiredArgsConstructor;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.sbrf.sb.service.KeycloakTokenService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RequestResponseLoggingFilter implements Filter {

    @Autowired
    private final KeycloakTokenService keycloakTokenVerifier;
 
    @Override
    public void doFilter(
      ServletRequest request,
      ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
 
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        try {
            String token = fetchTokenFromHeader(req);
            AccessToken accessToken = keycloakTokenVerifier.authenticateFromAuthHeader(token);

            chain.doFilter(request, response);
        } catch (VerificationException e) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.getWriter().write(e.getMessage());
        }
    }

    private String fetchTokenFromHeader(HttpServletRequest req) {
        String token = req.getHeader("Authorization");

        if (token.isEmpty()) {
            new VerificationException("No token");
        }
        return token;
    }
}