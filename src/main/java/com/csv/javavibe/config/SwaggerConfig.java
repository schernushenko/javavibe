package com.csv.javavibe.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI configuration for the application.
 * Provides API documentation accessible at /swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("JavaVibeOllamaTest API")
                .version("1.0.0")
                .description("API documentation for JavaVibeOllamaTest application with Kafka integration")
                .contact(new Contact()
                    .name("JavaVibe Team")
                    .email("support@javavibe.example.com")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Local Development Server")
            ));
    }
}