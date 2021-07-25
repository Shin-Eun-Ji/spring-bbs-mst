package com.example.springbbsmst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringBbsMstApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBbsMstApplication.class, args);
	}

}
