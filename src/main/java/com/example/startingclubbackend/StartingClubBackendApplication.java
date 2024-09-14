package com.example.startingclubbackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StartingClubBackendApplication {

	public static void main(String[] args) {
//		Dotenv dotenv = Dotenv.configure()
//				.filename(".env") // Make sure this matches the file name
//				.load();
//
//		System.setProperty("spring.mail.username", dotenv.get("MAIL_USERNAME"));
//		System.setProperty("spring.mail.password", dotenv.get("MAIL_PASSWORD"));

		SpringApplication.run(StartingClubBackendApplication.class, args);
	}
}
