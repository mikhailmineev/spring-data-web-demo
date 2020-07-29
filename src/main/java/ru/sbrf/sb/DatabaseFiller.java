package ru.sbrf.sb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.sbrf.sb.domain.Employee;
import ru.sbrf.sb.domain.Project;
import ru.sbrf.sb.repository.EmployeeRepository;
import ru.sbrf.sb.repository.ProjectRepository;

import javax.sql.DataSource;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseFiller implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        Project p1 = projectRepository.save(Project.builder().name("project1").employees(new ArrayList<>()).build());
        Employee e1 = employeeRepository.save(Employee.builder().firstName("user1").projects(new ArrayList<>()).build());
        p1.getEmployees().add(e1);
        // не будет работать, так как ведущая связь у сущности Project
        // e1.getProjects().add(p1);
        // employeeRepository.save(e1);
        projectRepository.save(p1);
    }
}
