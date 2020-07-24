package ru.sbrf.sb.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sbrf.sb.domain.Employee;
import ru.sbrf.sb.repository.EmployeeRepository;
import ru.sbrf.sb.service.EmployeeService;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee getOneEmpoloyee(long id) {
        return employeeRepository.getOne(id);
    }

    @Override
    public List<Employee> getAllEmpoloyees() {
        return employeeRepository.findAll();
    }
}
