package com.shopix.oder.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("keycloak",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .description("Keycloak OAuth2")
                                        .flows(new OAuthFlows()
                                                .authorizationCode(new OAuthFlow()
                                                        .authorizationUrl("http://127.0.0.1:8080/realms/shopix/protocol/openid-connect/auth")
                                                        .tokenUrl("http://127.0.0.1:8080/realms/shopix/protocol/openid-connect/token")
                                                        .scopes(new Scopes()
                                                                .addString("openid", "OpenID")
                                                                .addString("profile", "Profile")
                                                                .addString("email", "Email")
                                                        )
                                                )
                                        )
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("keycloak"));
    }
}
