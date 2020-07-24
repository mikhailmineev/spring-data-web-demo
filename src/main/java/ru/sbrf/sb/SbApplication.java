package ru.sbrf.sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.sbrf.sb.domain.Employee;

@SpringBootApplication
public class SbApplication {

	public static void main(String[] args) {

		SpringApplication.run(SbApplication.class, args);
		Employee e = new Employee();
	}

}
