## CloudInsight User Service (Read/Write)

Spring Boot microservice that handles user-related functionality for CloudInsight, including SSO login initiation. The service integrates with Spring Cloud Config for externalized configuration and registers with Eureka for service discovery.

### Features

- **SSO initiation endpoint**: Validates email domain and returns the provider authorization URL
- **Domain allowlisting**: Checks whether the email domain is supported for SSO
- **Global error handling**: Consistent error responses for validation and domain errors
- **Spring Cloud**: Config Server integration and Eureka Client registration
- **Actuator**: Health and monitoring endpoints
- **Testing**: Unit tests with JUnit 5 and Mockito; JaCoCo coverage

### Tech Stack

- **Java**: 21
- **Framework**: Spring Boot 3.5.x
- **Cloud**: Spring Cloud 2025.x (Config, Eureka Client)
- **Database**: PostgreSQL (runtime dependency)
- **Build**: Maven (wrapper included)

---

### Prerequisites

- Java 21
- Git
- (Optional) Running Spring Cloud Config Server and Eureka Server
- (Optional) PostgreSQL instance (if running with persistent storage)

### Configuration

Configuration is sourced from Spring Cloud Config Server. The application expects the following environment variables (you can also use a local `.env` file via `spring-dotenv`):

- `SPRING_APPLICATION_NAME`: Logical name of the service (used for config lookup)
- `SPRING_CLOUD_CONFIG_URI`: Base URI of the Config Server, e.g. `http://localhost:8888`

Minimal `application.yml` (for reference):

```yaml
spring:
  application:
    name: ${SPRING_APPLICATION_NAME}
  config:
    import: 'configserver:'
  cloud:
    config:
      uri: ${SPRING_CLOUD_CONFIG_URI}
```

If you aren't using a Config Server, you can provide local `application.yml` properties directly and remove the `configserver` import for local-only runs.

Database connection (PostgreSQL) is typically provided via Config Server or local properties, e.g.:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/user_service
    username: user
    password: pass
  jpa:
    hibernate:
      ddl-auto: update
```

### Build and Run

Build the project:

```bash
./mvnw clean package
```

Run the service:

```bash
./mvnw spring-boot:run
```

The application registers with Eureka (if configured) and exposes Actuator endpoints at `/actuator`.

### API

- **POST** `/api/v1/auth/sso-login`

  - Initiates SSO login by validating the email domain and returning the provider authorization URL.
  - Request body:

    ```json
    { "email": "user@example.com" }
    ```

  - Success response (200):

    ```json
    {
      "status": "success",
      "message": "SSO login initiated",
      "data": "https://<app>/oauth2/authorization/<provider>"
    }
    ```

  - Validation error (400) when email is blank or missing:

    ```json
    {
      "status": "fail",
      "message": "Validation failed",
      "errors": [ ... ]
    }
    ```

  - Domain not supported (400):

    ```json
    {
      "status": "fail",
      "message": "Domain name is not supported.",
      "errors": null
    }
    ```

### Seeding and Data

The codebase contains seeders for roles and supported SSO domains. Seeding behavior may be wired through application startup or separate profiles. Ensure your configuration provides the expected seed data or enable the seeders as needed.

### Testing

Run tests with coverage:

```bash
./mvnw clean verify
```

This generates a JaCoCo report under `target/site/jacoco/index.html`.

### Project Structure (high level)

```
src/main/java/com/cloud_insight_pro/user_service
  ├─ controller/        # REST controllers (e.g., AuthController)
  ├─ service/           # Business logic (e.g., AuthService)
  ├─ dto/               # Request/response DTOs
  ├─ model/             # JPA entities
  ├─ repository/        # Spring Data repositories
  ├─ exceptions/        # Exceptions and global handler
  ├─ seeder/            # Optional seeders for roles/domains
  └─ UserServiceApplication.java

src/main/resources
  └─ application.yml    # Config Server import and bootstrap
```

### Actuator

With the Actuator dependency, common endpoints include:

- `/actuator/health`

Expose and secure actuator endpoints via your configuration as needed.

### Notes

- The service expects a Config Server and Eureka in most deployments; for purely local development you can disable them and provide local properties.
- SSO providers and authorization endpoints are computed via `AuthService` based on email domain and provider registration.

### License

Proprietary – for internal use within the CloudInsight project unless stated otherwise.
