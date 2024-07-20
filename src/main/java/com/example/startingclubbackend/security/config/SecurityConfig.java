package com.example.startingclubbackend.security.config;

import com.example.startingclubbackend.repository.TokenRepository;
import com.example.startingclubbackend.security.JWT.JWTAuthenticationFilter;
import com.example.startingclubbackend.security.JWT.JWTService;
import com.example.startingclubbackend.security.utility.CustomUserDetailsService;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.nio.channels.FileLock;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
     private final CustomUserDetailsService customUserDetailsService ;
     private final JWTAuthenticationFilter jwtAuthenticationFilter ;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
          return http
                  .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                  .csrf(AbstractHttpConfigurer::disable)
                  .authorizeHttpRequests(
                          req -> req.requestMatchers("api/v1/auth/login", "api/v1/auth/register" ,"api/v1/auth/refresh/**" )

                                  .permitAll()
                                  .anyRequest()
                                  .authenticated())

                  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  .userDetailsService(customUserDetailsService)
                  .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class)
                  .build() ;
     }

     @Bean
     public CorsConfigurationSource corsConfigurationSource(){
          CorsConfiguration configuration= new CorsConfiguration() ;
          configuration.setAllowedOrigins(List.of("http://localhost:4200"));
          configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
          configuration.setAllowedHeaders(Arrays.asList("Authorization" ,"Content-Type"));
          configuration.setAllowCredentials(true);

          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("api/v1/**",configuration);
          return source ;
     }

     @Bean
     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws  Exception{
         return authenticationConfiguration.getAuthenticationManager() ;
     }
     @Bean
     public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
     }
}
