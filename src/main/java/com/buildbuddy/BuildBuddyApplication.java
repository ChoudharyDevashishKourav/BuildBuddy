package com.buildbuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BuildBuddyApplication {
	public static void main(String[] args) {
		SpringApplication.run(BuildBuddyApplication.class, args);
	}
}