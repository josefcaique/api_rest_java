package com.josef.api_rest.integrationtests.controllers.withjson;

import com.josef.api_rest.config.TestConfigs;
import com.josef.api_rest.integrationtests.dto.AccountCredentialsDTO;
import com.josef.api_rest.integrationtests.dto.BookDTO;
import com.josef.api_rest.integrationtests.dto.TokenDTO;
import com.josef.api_rest.integrationtests.dto.wrapper.BookEmbeddedDTO;
import com.josef.api_rest.integrationtests.dto.wrapper.WrapperBookDTO;
import com.josef.api_rest.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static BookDTO book;
    private static TokenDTO tokenDto;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookDTO();
        tokenDto = new TokenDTO();
    }

    @Test
    @Order(0)
    void signin() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("leandro", "admin123");

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
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .setBasePath("/api/book/v1")
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
        mockBook();

        var content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
            .when()
                .post()
            .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
                .body()
                    .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        Assertions.assertNotNull(createdBook.getId());
        Assertions.assertNotNull(book.getId());
        Assertions.assertEquals("Docker Deep Dive", book.getTitle());
        Assertions.assertEquals("Nigel Poulton", book.getAuthor());
        Assertions.assertEquals(55.99, book.getPrice());
    }
    
    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {

        book.setTitle("Docker Deep Dive - Updated");

        var content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
            .when()
                .put()
            .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
                .body()
                    .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        Assertions.assertNotNull(createdBook.getId());
        Assertions.assertTrue(createdBook.getId() > 0);

        Assertions.assertNotNull(createdBook.getId());
        Assertions.assertNotNull(book.getId());
        Assertions.assertEquals("Docker Deep Dive - Updated", book.getTitle());
        Assertions.assertEquals("Nigel Poulton", book.getAuthor());
        Assertions.assertEquals(55.99, book.getPrice());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", book.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        Assertions.assertNotNull(createdBook.getId());
        Assertions.assertTrue(createdBook.getId() > 0);

        Assertions.assertNotNull(createdBook.getId());
        Assertions.assertNotNull(book.getId());
        Assertions.assertEquals("Docker Deep Dive - Updated", book.getTitle());
        Assertions.assertEquals("Nigel Poulton", book.getAuthor());
        Assertions.assertEquals(55.99, book.getPrice());
    }

    @Test
    @Order(4)
    void deleteTest() throws JsonProcessingException {

        given(specification)
                .pathParam("id", book.getId())
            .when()
                .delete("{id}")
            .then()
                .statusCode(204);
    }


    @Test
    @Order(5)
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 9 , "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        // List<BookDTO> books = objectMapper.readValue(content, new TypeReference<List<BookDTO>>() {});
        WrapperBookDTO wrapper = objectMapper.readValue(content, WrapperBookDTO.class);

        List<BookDTO> books = wrapper.getEmbedded().getBooks();

        BookDTO bookOne = books.get(0);

        Assertions.assertNotNull(bookOne.getId());
        Assertions.assertNotNull(bookOne.getTitle());
        Assertions.assertNotNull(bookOne.getAuthor());
        Assertions.assertNotNull(bookOne.getPrice());
        Assertions.assertTrue(bookOne.getId() > 0);
        Assertions.assertEquals("The Art of Agile Development", bookOne.getTitle());
        Assertions.assertEquals("James Shore e Shane Warden", bookOne.getAuthor());
        Assertions.assertEquals(97.21, bookOne.getPrice());

        BookDTO foundBookSeven = books.get(7);

        Assertions.assertNotNull(foundBookSeven.getId());
        Assertions.assertNotNull(foundBookSeven.getTitle());
        Assertions.assertNotNull(foundBookSeven.getAuthor());
        Assertions.assertNotNull(foundBookSeven.getPrice());
        Assertions.assertTrue(foundBookSeven.getId() > 0);
        Assertions.assertEquals("The Art of Computer Programming, Volume 1: Fundamental Algorithms", foundBookSeven.getTitle());
        Assertions.assertEquals("Donald E. Knuth", foundBookSeven.getAuthor());
        Assertions.assertEquals(139.69, foundBookSeven.getPrice());
    }

    private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(Double.valueOf(55.99));
        book.setLaunchDate(new Date());
    }
}