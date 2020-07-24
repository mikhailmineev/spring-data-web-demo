package ru.sbrf.sb.service;

import ru.sbrf.sb.aspect.Logging;
import ru.sbrf.sb.domain.Employee;

import java.util.List;

public interface EmployeeService {
    Employee getOneEmpoloyee(long id);

    List<Employee> getAllEmpoloyees();
}
