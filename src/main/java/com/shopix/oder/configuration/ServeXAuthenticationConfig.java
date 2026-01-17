package com.shopix.oder.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ServeXAuthenticationConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();

        http
                // We use JWT, not sessions
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ---------------- CORS ----------------
                .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();

                        config.setAllowedOrigins(Arrays.asList(
                                "http://localhost:5173",
                                "http://localhost:4200",
                                "https://localhost:4200"
                        ));

                        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setAllowCredentials(true);
                        config.setMaxAge(3600L);

                        return config;
                    }
                }))
                // --------------------------------------

                // ---------------- CSRF ----------------
                .csrf(csrf -> csrf
                        .csrfTokenRequestHandler(csrfHandler)
                        // Stripe & internal endpoints do NOT use CSRF
                        .ignoringRequestMatchers("/user/seq", "/api/stripe/webhook")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                // --------------------------------------

                // ---------------- AUTH RULES ----------------
                .authorizeHttpRequests(auth -> auth
                        // 🔥 Stripe webhook MUST be open
                        .requestMatchers("/api/stripe/webhook").permitAll()

                        // Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // All API endpoints require JWT
                        .requestMatchers("/api/**").authenticated()

                        // Everything else
                        .anyRequest().permitAll()
                )
                // -------------------------------------------

                // ---------------- JWT / KEYCLOAK ----------------
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                )
                // ------------------------------------------------

                .formLogin(withDefaults());

        return http.build();
    }
}
