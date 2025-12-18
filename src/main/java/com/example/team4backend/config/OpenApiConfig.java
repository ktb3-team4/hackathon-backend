package com.example.team4backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {
    @Bean
    OpenAPI openApi() {
        String bearerSchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("team4 API")
                        .version("v1")
                        .description("team4 API 문서"))
                .components(new Components()
                        .addSecuritySchemes(bearerSchemeName,
                                new SecurityScheme()
                                        .name(bearerSchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("Bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(bearerSchemeName));
    }
}
