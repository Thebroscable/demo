package com.example.demo.controller;

import com.example.demo.filter.RequestCounterFilter;
import com.example.demo.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class RequestCounterControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RequestCounterFilter.resetRequestCount();
    }

    @Test
    void shouldReturnRequestCount() {
        given().baseUri("http://localhost").port(port)
            .when()
                .get("/book")
            .then()
                .statusCode(HttpStatus.OK.value());

        given().baseUri("http://localhost").port(port)
            .when()
                .get("/request-count")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("2"));
    }
}
