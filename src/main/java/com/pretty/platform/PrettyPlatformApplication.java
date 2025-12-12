package com.pretty.platform;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PrettyPlatformApplication implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(PrettyPlatformApplication.class);

    public static void main(String[] args) {
        // Load .env file before starting Spring Boot
        try {
            Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .ignoreIfMissing()
                .load();

            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
                System.out.println("Loaded env variable: " + entry.getKey() + " = " +
                    (entry.getKey().contains("SECRET") ? "***" : entry.getValue()));
            });

            System.out.println("Successfully loaded .env file");
        } catch (Exception e) {
            System.err.println("Warning: Could not load .env file: " + e.getMessage());
        }

        SpringApplication.run(PrettyPlatformApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Environment env = event.getApplicationContext().getEnvironment();
        String port = env.getProperty("server.port", "8080");
        if (event.getApplicationContext() instanceof WebServerApplicationContext webServerContext
                && webServerContext.getWebServer() != null) {
            port = String.valueOf(webServerContext.getWebServer().getPort());
        }
        String swaggerUiPath = env.getProperty("springdoc.swagger-ui.path", "/swagger-ui.html");
        String apiDocsPath = env.getProperty("springdoc.api-docs.path", "/v3/api-docs");
        logger.info("Swagger UI available at http://localhost:{}{}", port, swaggerUiPath);
        logger.info("OpenAPI docs available at http://localhost:{}{}", port, apiDocsPath);
    }
}
