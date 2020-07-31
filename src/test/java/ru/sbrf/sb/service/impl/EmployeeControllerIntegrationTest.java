package ru.sbrf.sb.service.impl;

import org.junit.jupiter.api.Test;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.sbrf.sb.domain.Employee;
import ru.sbrf.sb.repository.EmployeeRepository;
import ru.sbrf.sb.service.KeycloakTokenService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @MockBean
    private KeycloakTokenService keycloakTokenService;

    @Test
    @Transactional
    public void getEmployees_ok() throws VerificationException {
        // Готовим заглушку
        AccessToken accessToken = new AccessToken();
        accessToken.setRealmAccess(new AccessToken.Access());
        accessToken.getRealmAccess().addRole("read-employers");
        Mockito.when(keycloakTokenService.authenticateFromAuthHeader("Bearer testToken")).thenReturn(accessToken);
        // Готовим запрос
        Employee employee = Employee.builder().firstName("user1000").build();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer testToken");
        HttpEntity<Employee> request = new HttpEntity<>(employee, headers);

        ResponseEntity<Employee> response = testRestTemplate.postForEntity("/sb/employee/", request, Employee.class);

        // Проверка ответа
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getFirstName(), employee.getFirstName());
        // Проверка сохраненной сущности
        Employee saved = employeeRepository.getOne(response.getBody().getId());
        assertNotNull(saved);
        assertEquals(saved.getFirstName(), employee.getFirstName());
    }
}
