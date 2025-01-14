package com.itgirl.library_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.itgirl.library_project.entity")
public class LibraryProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(LibraryProjectApplication.class, args);
	}
}

