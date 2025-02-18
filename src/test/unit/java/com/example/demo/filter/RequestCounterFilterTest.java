package com.example.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class RequestCounterFilterTest {

    @BeforeEach
    void setUp() {
        RequestCounterFilter.resetRequestCount();
    }

    @Test
    void shouldIncrementRequestCount() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        assertEquals(0, RequestCounterFilter.getRequestCount());

        new RequestCounterFilter().doFilter(request, response, chain);

        assertEquals(1, RequestCounterFilter.getRequestCount());
    }
}
