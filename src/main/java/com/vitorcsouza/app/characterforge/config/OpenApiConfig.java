package com.vitorcsouza.app.characterforge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("D&D Character Forge API")
                        .version("v1")
                        .description("REST API for creating, managing, and validating D&D 5e character sheets.")
                        .contact(new Contact()
                                .name("Vítor Cavalcante Souza")
                                .url("https://github.com/vitorcsouza")));
    }
}
