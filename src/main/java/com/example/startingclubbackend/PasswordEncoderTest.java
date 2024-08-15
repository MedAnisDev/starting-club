package com.example.startingclubbackend;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Scanner;

public class PasswordEncoderTest {

    public static void main(String[] args) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a password to encode:");
        String rawPassword =scanner.nextLine() ;
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("original Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
        System.out.println(passwordEncoder.matches(rawPassword , encodedPassword));
    }
}
