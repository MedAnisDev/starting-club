package com.example.startingclubbackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StartingClubBackendApplication {

	public static void main(String[] args) {
		// Load the .env file
		Dotenv dotenv = Dotenv.configure()
				.filename("secret.env")
				.load();

		// Set system properties from .env file
		System.setProperty("spring.mail.username", dotenv.get("MAIL_USERNAME"));
		System.setProperty("spring.mail.password", dotenv.get("MAIL_PASSWORD"));
		System.setProperty("jwt.access_secret_key", dotenv.get("JWT_ACCESS_SECRET_KEY"));
		System.setProperty("jwt.refresh_secret_key", dotenv.get("JWT_REFRESH_SECRET_KEY"));
		System.setProperty("jwt.refresh_expiration", dotenv.get("REFRESH_EXPIRATION_DURATION"));
		System.setProperty("jwt.access_expiration", dotenv.get("ACCESS_EXPIRATION_DURATION"));

		SpringApplication.run(StartingClubBackendApplication.class, args);
	}

}
