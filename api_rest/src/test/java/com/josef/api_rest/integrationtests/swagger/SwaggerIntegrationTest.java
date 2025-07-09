package com.josef.api_rest.integrationtests.swagger;

import com.josef.api_rest.config.TestConfigs;
import com.josef.api_rest.integrationtests.dto.AccountCredentialsDTO;
import com.josef.api_rest.integrationtests.dto.TokenDTO;
import com.josef.api_rest.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static TokenDTO tokenDto;

    @BeforeAll
    static void setUp() {
        tokenDto = new TokenDTO();
    }

    @Test
    @Order(1)
    void signin() {
        AccountCredentialsDTO credentials = AccountCredentialsDTO.createAccountCredentialsDTO("leandro", "admin123");
        tokenDto = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);


        Assertions.assertNotNull(tokenDto.getAccessToken());
        Assertions.assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(2)
    void shouldDisplaySwaggerUIPage() {
        var content = given()
            .basePath("/swagger-ui")
                .port(TestConfigs.SERVER_PORT)
                .header("Authorization", "Bearer " +tokenDto.getAccessToken())
            .when()
                .get("/index.html")
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();

        Assertions.assertTrue(content.contains("Swagger UI"));
    }

    @Test
    @Order(3)
    void shouldAccessSwaggerDocsJson() {
        given()
                .port(TestConfigs.SERVER_PORT)
                .when()
                .get("/v3/api-docs")
                .then()
                .statusCode(200);
    }

}