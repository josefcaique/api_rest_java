package com.josef.api_rest.integrationtests.controllers.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.josef.api_rest.config.TestConfigs;
import com.josef.api_rest.integrationtests.dto.PersonDTO;
import com.josef.api_rest.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_JOSEF)
                .setBasePath("api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();


        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson =  objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        Assertions.assertNotNull(createdPerson.getId());
        Assertions.assertTrue(createdPerson.getId() > 0);

        Assertions.assertEquals("Linus", createdPerson.getFirstName());
        Assertions.assertEquals("Torvalds", createdPerson.getLastName());
        Assertions.assertEquals("Helsinki - Finland", createdPerson.getAddress());
        Assertions.assertEquals("M", createdPerson.getGender());
        Assertions.assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        person.setLastName("Benedict Torvalds");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson =  objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        Assertions.assertNotNull(createdPerson.getId());
        Assertions.assertTrue(createdPerson.getId() > 0);

        Assertions.assertEquals("Linus", createdPerson.getFirstName());
        Assertions.assertEquals("Benedict Torvalds", createdPerson.getLastName());
        Assertions.assertEquals("Helsinki - Finland", createdPerson.getAddress());
        Assertions.assertEquals("M", createdPerson.getGender());
        Assertions.assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson =  objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        Assertions.assertNotNull(createdPerson.getId());
        Assertions.assertTrue(createdPerson.getId() > 0);

        Assertions.assertEquals("Linus", createdPerson.getFirstName());
        Assertions.assertEquals("Benedict Torvalds", createdPerson.getLastName());
        Assertions.assertEquals("Helsinki - Finland", createdPerson.getAddress());
        Assertions.assertEquals("M", createdPerson.getGender());
        Assertions.assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void disablePersonTest() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson =  objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        Assertions.assertNotNull(createdPerson.getId());
        Assertions.assertTrue(createdPerson.getId() > 0);

        Assertions.assertEquals("Linus", createdPerson.getFirstName());
        Assertions.assertEquals("Benedict Torvalds", createdPerson.getLastName());
        Assertions.assertEquals("Helsinki - Finland", createdPerson.getAddress());
        Assertions.assertEquals("M", createdPerson.getGender());
        Assertions.assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void deletePersonTest() throws JsonProcessingException {
        var content = given(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }

}