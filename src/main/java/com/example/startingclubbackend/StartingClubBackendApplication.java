package com.example.startingclubbackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StartingClubBackendApplication {

	public static void main(String[] args) {
		// Load the .env file
		Dotenv dotenv = Dotenv.configure()
				.filename("secret.env")
				.load();

		// Set system properties from .env file
		System.setProperty("spring.mail.username", dotenv.get("MAIL_USERNAME"));
		System.setProperty("spring.mail.password", dotenv.get("MAIL_PASSWORD"));

		SpringApplication.run(StartingClubBackendApplication.class, args);
	}

}
