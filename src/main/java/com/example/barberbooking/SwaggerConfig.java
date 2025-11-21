package com.example.barberbooking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/*
Swagger UI: http://localhost:8080/swagger-ui.html
*/

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Simple CRUD API")
                .version("1.0")
                .description("Documentation of the API using Swaager OpenAPI"));
    }
}
