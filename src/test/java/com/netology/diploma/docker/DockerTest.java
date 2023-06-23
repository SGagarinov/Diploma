package com.netology.diploma.docker;

import com.netology.diploma.dto.auth.AuthRequest;
import com.netology.diploma.dto.auth.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class DockerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>
            ("postgres:13.2")
            .withDatabaseName("diploma")
            .withUsername("postgres")
            .withPassword("123456");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
        registry.add("integration-tests-db", postgresDB::getDatabaseName);

    }

    @Container
    private GenericContainer<?> diplomaApp = new GenericContainer<>("diploma:1.0")
            .withExposedPorts(5555)
            .withEnv("spring.datasource.url", postgresDB.getJdbcUrl())
            .withEnv("spring.datasource.username", postgresDB.getUsername())
            .withEnv("spring.datasource.password", postgresDB.getPassword());

    @BeforeEach
    void setUp() {
        postgresDB.start();
        diplomaApp.start();
    }

    @Test
    void postTest() {
        Integer port = diplomaApp.getMappedPort(5555);
        String user = "admin";
        String password = "thisismyadminpassword";
        AuthRequest request = new AuthRequest(user, password);

        ResponseEntity<AuthResponse> login = restTemplate.postForEntity("http://localhost:" + port + "/login", request, AuthResponse.class);
        System.out.println(login.getBody().getAuthToken());
        assertNotNull(login.getBody().getAuthToken());
    }
}
