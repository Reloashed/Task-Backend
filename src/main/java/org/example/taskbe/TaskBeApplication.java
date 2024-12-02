package org.example.taskbe;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition
@SpringBootApplication
public class TaskBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskBeApplication.class, args);
	}

}
