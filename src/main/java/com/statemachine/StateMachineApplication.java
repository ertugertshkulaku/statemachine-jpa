package com.statemachine;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ComponentScan
public class StateMachineApplication {
	public static void main(String[] args) {
		SpringApplication.run(StateMachineApplication.class, args);
	}
}
