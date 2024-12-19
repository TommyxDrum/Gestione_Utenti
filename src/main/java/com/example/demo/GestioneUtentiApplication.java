package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.demo")
public class GestioneUtentiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestioneUtentiApplication.class, args);
	}

}