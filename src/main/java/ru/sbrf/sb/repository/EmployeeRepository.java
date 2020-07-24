package ru.sbrf.sb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sbrf.sb.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
