package com.rewardly.emp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Value("${app.version:v1.0}")
    private String appVersion;
    
    @Value("${swagger.enabled:true}")
    private boolean swaggerEnabled;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Rewardly Employee Management Microservices")
                .version(appVersion)
                .description("Comprehensive API documentation for Rewardly employee management system. " +
                            "This microservice handles employee operations, rewards tracking, and management functionalities.")
                .contact(new Contact()
                    .name("Rewardly Team")
                    .email("team@rewardly.in")
                    .url("https://rewardly.in"))
                .license(new License()
                    .name("Proprietary")
                    .url("https://rewardly.in/license")))
            .servers(List.of(
                new Server().url("https://api.rewardly.in").description("Production Server"),
                new Server().url("https://staging-api.rewardly.in").description("Staging Server"),
                new Server().url("http://localhost:8082").description("Local Development Server")
            ))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", 
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter JWT token obtained from /auth/login endpoint")));
    }
    
    @Bean
    public GroupedOpenApi employeeApi() {
        return GroupedOpenApi.builder()
            .group("employee-management")
            .pathsToMatch("/api/v1/employees/**")
            .displayName("Employee Management APIs")
            .build();
    }
    
    @Bean
    public GroupedOpenApi bonusApi() {
        return GroupedOpenApi.builder()
            .group("bonus")
            .pathsToMatch("/api/bonus/**")
            .displayName("Recognition of employee bonus APIs")
            .build();
    }
    
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
            .group("authentication")
            .pathsToMatch("/api/auth/**")
            .displayName("Authentication APIs")
            .build();
    }
    
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("all-apis")
            .pathsToMatch("/api/**")
            .displayName("All Public APIs")
            .build();
    }
}