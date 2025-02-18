package com.example.demo.controller;

import com.example.demo.api.RequestCountApi;
import com.example.demo.filter.RequestCounterFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestCounterController implements RequestCountApi {

    @Override
    public ResponseEntity<Long> getRequestCount() {
        return ResponseEntity.ok(RequestCounterFilter.getRequestCount());
    }
}
