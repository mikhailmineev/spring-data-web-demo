package ru.sbrf.sb.service.impl;

import org.junit.jupiter.api.Test;
import org.keycloak.common.VerificationException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import ru.sbrf.sb.controller.EmployeeController;
import ru.sbrf.sb.domain.Employee;
import ru.sbrf.sb.repository.EmployeeRepository;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TransactionService transactionService;

    @Test
    public void getEmployees_ok() {
        Employee employee = Employee.builder().firstName("user1000").build();
        ResponseEntity<Employee> response = testRestTemplate.postForEntity("/sb/employee/", employee, Employee.class);

        Employee saved = transactionService.findByName("user1000");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getFirstName(), employee.getFirstName());
        assertNotNull(saved);

    }
}
