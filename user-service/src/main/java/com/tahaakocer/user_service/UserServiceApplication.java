package com.tahaakocer.user_service;

import com.tahaakocer.user_service.repository.h2.H2UserRepository;
import com.tahaakocer.user_service.repository.postgres.PostgresUserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
