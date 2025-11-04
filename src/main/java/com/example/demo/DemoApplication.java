package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(scanBasePackages = {"com.example.demo", "com.example.controller", "com.example.db"})
@RestController
@EnableJpaRepositories("com.example.db")
@EntityScan("com.example.db")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/bonjour")
	public String hello(){
		return String.format("Bonjour le monde !");
	}
	
}
