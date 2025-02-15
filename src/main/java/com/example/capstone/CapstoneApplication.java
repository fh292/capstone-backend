package com.example.capstone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.capstone.services.PlanService;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class CapstoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeData(PlanService planService) {
		return args -> {
			// Initialize default plans
			planService.initializeDefaultPlans();
		};
	}

}
