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

3. Open your browser and go to `http://localhost:8080/swagger-ui.html` to access the Swagger UI.

4. You can test the `/hello` endpoint from the Swagger UI.

## API Endpoints

- `GET /hello` - Returns a "Hello, World!" message.

## Configuration

The application is configured to run on port 8080 by default. You can change this in `src/main/resources/application.properties`.

## Building the Project

To build the project, run:

```bash
mvn clean install
```

This will create a JAR file in the `target` directory that you can run with `java -jar target/demo-0.0.1-SNAPSHOT.jar`.