package com.josef.api_rest.integrationtests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.36")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
                .withUrlParam("useSSL", "false");

        private void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join();
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            Map<String, Object> props = Map.of(
                    "spring.datasource.url", mysql.getJdbcUrl(),
                    "spring.datasource.username", mysql.getUsername(),
                    "spring.datasource.password", mysql.getPassword(),
                    "spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver"
            );
            environment.getPropertySources().addFirst(new MapPropertySource("testcontainers", props));
        }
    }
}
