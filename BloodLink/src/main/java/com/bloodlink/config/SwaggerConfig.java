package com.bloodlink.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bloodLinkOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("BloodLink API")
                        .description("Blood Donor Registry & Search API built with Spring Boot 3")
                        .version("v1.0.0"));
    }
}
