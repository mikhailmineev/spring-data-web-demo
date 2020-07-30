package ru.sbrf.sb.service.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.sb.domain.Employee;
import ru.sbrf.sb.repository.EmployeeRepository;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
public class TransactionService {
    public final EmployeeRepository employeeRepository;

    public Employee findByName(String name) {
        Employee prototype = Employee.builder().firstName(name).build();
        return employeeRepository.findOne(Example.of(prototype)).orElse(null);
    }

    public Employee get(Long id) {
        return (Employee) Hibernate.unproxy(employeeRepository.getOne(id));
    }
}
