package com.example.departmentservice;

import org.springframework.boot.SpringApplication;

public class TestDepartmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(DepartmentServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
