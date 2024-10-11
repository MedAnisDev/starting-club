package com.example.startingclubbackend.security.utility;

import io.github.cdimascio.dotenv.Dotenv;

public class SecurityConstants {
    static Dotenv dotenv = Dotenv.configure()
            .filename(".env") // Make sure this matches the file name
            .load();

    public static final long ACCESS_JWT_EXPIRATION   = Long.parseLong(dotenv.get("ACCESS_JWT_EXPIRATION"));
    public static final long REFRESH_JWT_EXPIRATION  = Long.parseLong(dotenv.get("REFRESH_JWT_EXPIRATION"));
    public static final String ACCESS_JWT_SECRET_KEY = dotenv.get("ACCESS_JWT_SECRET_KEY");
    public static final String REFRESH_JWT_SECRET_KEY= dotenv.get("REFRESH_JWT_SECRET_KEY");

//    public static final long ACCESS_JWT_EXPIRATION   = Long.parseLong(System.getenv("ACCESS_JWT_EXPIRATION"));
//    public static final long REFRESH_JWT_EXPIRATION  = Long.parseLong(System.getenv("REFRESH_JWT_EXPIRATION"));
//    public static final String ACCESS_JWT_SECRET_KEY = System.getenv("ACCESS_JWT_SECRET_KEY");
//    public static final String REFRESH_JWT_SECRET_KEY= System.getenv("REFRESH_JWT_SECRET_KEY");

//    public static final long ACCESS_JWT_EXPIRATION   =86400000 ;
//    public static final long REFRESH_JWT_EXPIRATION  =604800000;
//    public static final String ACCESS_JWT_SECRET_KEY ="qH/KeTPBzgY6NMl84dqn2LmJX1ahtrkj+WkIVyn+zIHthUhogYvNhwQfj/ZwDq8W";
//    public static final String REFRESH_JWT_SECRET_KEY="Sx1ulId2RUJcYU4Totv+WtlhZ1JK2zPXzlpReWbWu6okP2aNHV7qvQw9ck26NkHb";



}
