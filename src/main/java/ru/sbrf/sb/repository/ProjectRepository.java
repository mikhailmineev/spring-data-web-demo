package ru.sbrf.sb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sbrf.sb.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
