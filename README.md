# Shopix Order Service

## Overview
Manages order creation and updates, publishes order events to Kafka, and calls
the User service over gRPC for user context.

## Tech and Dependencies
- Java 21 (see `pom.xml`)
- Spring Boot, Spring Data JPA
- MySQL (`shopix_db`)
- Kafka producer
- gRPC client to User service (`localhost:9090`)
- Stripe API for payments
- OAuth2 resource server (Keycloak)

## Configuration
Edit `src/main/resources/application.properties` as needed.

Key settings:
- `server.port=8082`
- `spring.datasource.url=jdbc:mysql://localhost:3306/shopix_db`
- `spring.kafka.bootstrap-servers=localhost:9092`
- `grpc.client.user-service.address=static://localhost:9090`
- `stripe.secret-key` and `stripe.webhook-secret`
- `spring.security.oauth2.resourceserver.jwt.jwk-set-uri`

Note: Replace the sample Stripe keys and configure your Keycloak realm before
use.

## API Docs
Swagger UI is enabled:
- `http://localhost:8082/swagger-ui.html`

## Run
From this directory:

```bash
./mvnw spring-boot:run
```
