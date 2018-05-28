package com.patsnap.inspad.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.patsnap.inspad" })
@EnableAutoConfiguration
public class InspadServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InspadServiceApplication.class, args);
	}
}
