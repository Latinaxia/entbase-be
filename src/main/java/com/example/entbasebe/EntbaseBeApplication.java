package com.example.entbasebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EntbaseBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntbaseBeApplication.class, args);
	}

}
