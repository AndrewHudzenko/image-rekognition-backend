package com.correnet.imagerekognition.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("Correnet Image Rekognition API")
                        .version("1.0")
                        .description("Correnet Image Rekognition API for managing images")
                        .license(new License().name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(List.of(new Server().url("http://localhost:8080")));

    }
}
