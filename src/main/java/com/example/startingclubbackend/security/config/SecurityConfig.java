package com.example.startingclubbackend.security.config;

import com.example.startingclubbackend.security.JWT.JWTAuthenticationFilter;
import com.example.startingclubbackend.security.JWT.JwtAuthEntryPoint;
import com.example.startingclubbackend.security.utility.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
     private final CustomUserDetailsService customUserDetailsService ;
     private final JWTAuthenticationFilter jwtAuthenticationFilter ;
    private final JwtAuthEntryPoint authEntryPoint;
    private final LogoutHandler logoutHandler;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JWTAuthenticationFilter jwtAuthenticationFilter, JwtAuthEntryPoint authEntryPoint, LogoutHandler logoutHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authEntryPoint = authEntryPoint;
        this.logoutHandler = logoutHandler;
    }

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "configuration/ui",
            "configuration/security"
    };

    @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
          return http
                  .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                  .csrf(AbstractHttpConfigurer::disable)
                  .exceptionHandling(
                          exceptions -> exceptions.authenticationEntryPoint(authEntryPoint)
                  )
                  .authorizeHttpRequests(
                          req -> req.requestMatchers("api/v1/auth/**")
                                    .permitAll()

                                  .requestMatchers(SWAGGER_WHITELIST)
                                    .permitAll()

                                  .requestMatchers(HttpMethod.GET, "/ws/**")
                                  .permitAll()

                                  .requestMatchers(HttpMethod.GET, "api/v1/athlete/admin/**")
                                    .hasAnyRole("SUPER_ADMIN", "ADMIN")

                                  .requestMatchers(HttpMethod.POST, "/api/v1/announcements/**" ,
                                          "/api/v1/events/**",
                                          "api/v1/files/**",
                                          "api/v1/athlete_event/**" ,
                                          "api/v1/training_session/**").
                                    hasAnyRole("SUPER_ADMIN", "ADMIN")

                                  .requestMatchers(HttpMethod.PUT,
                                          "/api/v1/announcements/**" ,
                                          "/api/v1/events/**",
                                          "api/v1/files/**",
                                          "api/v1/athlete_event/**" ,
                                          "api/v1/training_session/**",
                                          "api/v1/athlete/admin/**")
                                    .hasAnyRole("SUPER_ADMIN", "ADMIN")

                                  .requestMatchers(HttpMethod.DELETE,
                                          "/api/v1/announcements/**",
                                          "/api/v1/events/**" ,
                                          "/api/v1/register_event/admin/**",
                                          "api/v1/files/**" ,
                                          "api/v1/athlete/**",
                                          "api/v1/athlete_event/**" ,
                                          "api/v1/training_session/**")
                                    .hasRole("SUPER_ADMIN")

                                  .requestMatchers(HttpMethod.POST, "api/v1/register_event/**")
                                    .hasRole("ATHLETE")

                                  .anyRequest().authenticated()
                  )
                  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                  .userDetailsService(customUserDetailsService)
                  .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class)
                  .logout(
                          logout -> logout
                                  .logoutUrl("/api/v1/auth/logout")
                                  .addLogoutHandler(logoutHandler)
                                  .logoutSuccessHandler(((request, response, authentication) ->{
                                      response.setStatus(HttpServletResponse.SC_OK);
                                      response.getWriter().write("{" +
                                              "\"message\": \"Logout successful\"" +
                                              "}");
                                  }))
                  )
                  .build() ;
     }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws  Exception{
        return authenticationConfiguration.getAuthenticationManager() ;
    }

     @Bean
     public CorsConfigurationSource corsConfigurationSource(){
          CorsConfiguration configuration= new CorsConfiguration() ;
          configuration.setAllowedOrigins(List.of("http://localhost:3000"));
          configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
          configuration.setAllowedHeaders(Arrays.asList("Authorization" ,"Content-Type"));
          configuration.setAllowCredentials(true);

          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/**",configuration);
          return source ;
     }

     @Bean
     public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
     }
}
