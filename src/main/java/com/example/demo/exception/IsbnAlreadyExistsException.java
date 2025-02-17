package com.example.demo.exception;

public class IsbnAlreadyExistsException extends RuntimeException {
    public IsbnAlreadyExistsException(String isbn) {
        super(String.format("A book with ISBN '%s' already exists.", isbn));
    }
}
