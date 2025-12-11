# Demo Spring Boot Project with Swagger

This is a simple Spring Boot application with Swagger/OpenAPI documentation.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Running the Application

1. Clone or navigate to the project directory.

2. Run the following command to start the application:

   ```bash
   mvn spring-boot:run
   ```

3. Open your browser and go to `http://localhost:8082/swagger-ui/index.html` to access the Swagger UI (OpenAPI docs are also available at `http://localhost:8082/api-docs`).

4. You can test the `/hello` endpoint from the Swagger UI.

## API Endpoints

- `GET /hello` - Returns a "Hello, World!" message.

## Configuration

- `server.port=8082` – change the port if needed.
- `springdoc.api-docs.path=/api-docs` – OpenAPI JSON path consumed by Swagger UI.
- `springdoc.swagger-ui.path=/swagger-ui.html` – entry point for the UI (also reachable via `/swagger-ui/index.html`).

## Building the Project

To build the project, run:

```bash
mvn clean install
```

This will create a JAR file in the `target` directory that you can run with `java -jar target/pretty-0.0.1-SNAPSHOT.jar`.
