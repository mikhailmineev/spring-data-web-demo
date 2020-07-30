package ru.sbrf.sb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.sb.domain.Employee;
import ru.sbrf.sb.service.EmployeeService;
import ru.sbrf.sb.service.KeycloakTokenService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("sb/employee")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    private final KeycloakTokenService keycloakTokenVerifier;

    @org.springframework.web.bind.annotation.ExceptionHandler(VerificationException.class)
    public String error(VerificationException e) {
        log.warn("Auth failed", e);
        return e.getMessage();
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmpoloyers(@RequestHeader(value = "Authorization", required = false) String authHeader) throws VerificationException {
        if (authHeader == null) {
            return ResponseEntity.badRequest().build();
        } else {
            AccessToken accessToken = keycloakTokenVerifier.authenticateFromAuthHeader(authHeader);

            String subject = accessToken.getSubject();
            boolean contains = accessToken.getRealmAccess().getRoles().contains("read-employers");
            if (!contains) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(employeeService.getAllEmpoloyees());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }
}
