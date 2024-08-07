package com.example.startingclubbackend.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@OpenAPIDefinition(
        info = @Info(
                title = "Starting-club API",
                version = "1.0",
                description = "This is the API documentation for starting-club, providing access to our services and data. Please refer to the terms of service and license information for usage guidelines."
        ),
        servers = {
                @Server(
                        url = "http://localhost:8082",
                        description = "Local development server"
                ),
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecuritySchemes({
        @SecurityScheme(
                name = "bearerAuth",
                type = SecuritySchemeType.HTTP,
                in = SecuritySchemeIn.HEADER,
                scheme = "bearer",
                bearerFormat = "JWT",
                description = "JWT Bearer Token for API authentication"
        ),
})
public class OpenApiConfig {
}