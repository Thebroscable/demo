package com.example.demo.controller;

import com.example.demo.models.BookRequest;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.entity.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.TestcontainersConfiguration;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;

    private Book savedBook;

    private RequestSpecification givenAuthenticatedUser() {
        return given().baseUri("http://localhost").port(this.port).contentType(ContentType.JSON)
                .auth().basic("user", "password");
    }

    @BeforeEach
    void setUp() {
        Book book = Book.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .publisher("Scribner")
                .isbn("9780743273565")
                .pages(180)
                .releaseDate(LocalDate.of(2012, 6, 12))
                .language("English")
                .description("A novel set in the Jazz Age...")
                .build();
        savedBook = bookRepository.save(book);
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldCreateBook() throws Exception {
        BookRequest bookRequest = new BookRequest()
                .title("1984")
                .author("George Orwell")
                .publisher("Secker & Warburg")
                .isbn("9780451524935")
                .pages(328)
                .language("English")
                .description("A dystopian novel...");

        givenAuthenticatedUser()
                .body(new ObjectMapper().writeValueAsString(bookRequest))
            .when()
                .post("/book")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", is(notNullValue()));
    }

    @Test
    void shouldReturnConflictForNonUniqueIsbn() throws Exception {
        BookRequest bookRequest = new BookRequest()
                .title("1984")
                .author("George Orwell")
                .publisher("Secker & Warburg")
                .isbn(savedBook.getIsbn());

        givenAuthenticatedUser()
                .body(new ObjectMapper().writeValueAsString(bookRequest))
            .when()
                .post("/book")
            .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void shouldReturnBadRequestForInvalidInput() throws Exception {
        BookRequest bookRequest = new BookRequest()
                .title("1984")
                .author("George Orwell")
                .publisher("Secker & Warburg")
                .isbn("1");  // Invalid ISBN

        givenAuthenticatedUser()
                .body(new ObjectMapper().writeValueAsString(bookRequest))
            .when()
                .post("/book")
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookRequest updatedBookRequest = new BookRequest()
                .title("1984")
                .author("George Orwell")
                .publisher("Secker & Warburg");

        givenAuthenticatedUser()
                .auth().basic("user", "password")
                .body(new ObjectMapper().writeValueAsString(updatedBookRequest))
            .when()
                .put("/book/{bookId}", savedBook.getId())
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", equalTo(savedBook.getId().intValue()));
    }

    @Test
    void shouldDeleteBook() {
        givenAuthenticatedUser()
            .when()
                .delete("/book/{bookId}", savedBook.getId())
            .then()
                .statusCode(HttpStatus.OK.value());

        givenAuthenticatedUser()
            .when()
                .get("/book/{bookId}", savedBook.getId())
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnBooksPaginated() {
        givenAuthenticatedUser()
                .param("page", "0")
                .param("size", "10")
            .when()
                .get("/book")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", is(not(empty())))
                .body("totalElements", equalTo(1));
    }
}
