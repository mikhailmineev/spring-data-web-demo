package ru.sbrf.sb.service.impl;

import ru.sbrf.sb.domain.Employee;

import java.util.List;

public interface EmployeeService {
    Employee getOneEmpoloyee(long id);

    List<Employee> getAllEmpoloyees();
}
