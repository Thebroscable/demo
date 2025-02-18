package com.example.demo.controller;

import com.example.demo.api.BookApi;
import com.example.demo.models.BookRequest;
import com.example.demo.models.BookResponse;
import com.example.demo.models.PaginatedBookResponse;
import com.example.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController implements BookApi {

    private final BookService bookService;

    @Override
    public ResponseEntity<Long> createBook(BookRequest body) {
        log.info("Request to create book, payload = {}", body);
        return ResponseEntity.ok(bookService.createBook(body));
    }

    @Override
    public ResponseEntity<Void> deleteBook(Long bookId) {
        log.info("Request to delete book, bookId = {}", bookId);
        bookService.deleteBookById(bookId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BookResponse> getBook(Long bookId) {
        log.info("Request to get book, bookId = {}", bookId);
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @Override
    public ResponseEntity<PaginatedBookResponse> searchBooks(Integer page,
                                                             Integer size,
                                                             String searchText) {
        log.info("Request to get paginated books, page = {}, size = {}, search text = {}",
                page, size, searchText);
        return ResponseEntity.ok(bookService.searchBook(page, size, searchText));
    }

    @Override
    public ResponseEntity<Long> updateBook(Long bookId, BookRequest body) {
        log.info("Request to update book, bookId = {}, payload = {}", bookId, body);
        return ResponseEntity.ok(bookService.updateBookById(bookId, body));
    }
}
