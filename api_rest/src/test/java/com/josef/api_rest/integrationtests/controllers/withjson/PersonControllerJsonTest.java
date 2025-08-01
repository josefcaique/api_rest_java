package com.josef.api_rest.integrationtests.controllers.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.josef.api_rest.config.TestConfigs;
import com.josef.api_rest.integrationtests.dto.AccountCredentialsDTO;
import com.josef.api_rest.integrationtests.dto.PersonDTO;
import com.josef.api_rest.integrationtests.dto.TokenDTO;
import com.josef.api_rest.integrationtests.dto.wrapper.WrapperPersonDTO;
import com.josef.api_rest.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;
    private static TokenDTO tokenDto;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
        tokenDto = new TokenDTO();
    }

    @Test
    @Order(0)
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

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_JOSEF)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " +tokenDto.getAccessToken())
                .setBasePath("api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Assertions.assertNotNull(tokenDto.getAccessToken());
        Assertions.assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();

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

    @Test
    @Order(6)
    void findAll() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 3,  "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();

        PersonDTO personOne =  people.get(0);
        person = personOne;

        Assertions.assertNotNull(personOne.getId());
        Assertions.assertTrue(personOne.getId() > 0);

        Assertions.assertEquals("Amby", personOne.getFirstName());
        Assertions.assertEquals("Fattore", personOne.getLastName());
        Assertions.assertEquals("Room 1219", personOne.getAddress());
        Assertions.assertEquals("M", personOne.getGender());
        Assertions.assertTrue(personOne.getEnabled());

        PersonDTO personFive =  people.get(4);
        person = personFive;

        Assertions.assertNotNull(personFive.getId());
        Assertions.assertTrue(personFive.getId() > 0);

        Assertions.assertEquals("Andreas", personFive.getFirstName());
        Assertions.assertEquals("Knobell", personFive.getLastName());
        Assertions.assertEquals("PO Box 27657", personFive.getAddress());
        Assertions.assertEquals("M", personFive.getGender());
        Assertions.assertTrue(personFive.getEnabled());
    }

    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }

}