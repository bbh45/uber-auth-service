package com.bb.uber_auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan("com.bb.uber_entity_service.models")
public class UberAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UberAuthServiceApplication.class, args);
	}

}
