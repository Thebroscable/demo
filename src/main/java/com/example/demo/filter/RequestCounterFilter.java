package com.example.demo.filter;

import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RequestCounterFilter implements Filter {

    private static final AtomicLong requestCount = new AtomicLong(0);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        requestCount.incrementAndGet();
        chain.doFilter(request, response);
    }

    public static Long getRequestCount() {
        return requestCount.get();
    }

    public static void resetRequestCount() {
        requestCount.set(0);
    }
}
