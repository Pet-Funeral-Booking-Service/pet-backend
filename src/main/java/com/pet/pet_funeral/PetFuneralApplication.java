package com.pet.pet_funeral;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PetFuneralApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetFuneralApplication.class, args);
	}

}
