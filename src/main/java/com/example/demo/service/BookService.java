package com.example.demo.service;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.exception.IsbnAlreadyExistsException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.models.BookRequest;
import com.example.demo.models.BookResponse;
import com.example.demo.models.PaginatedBookResponse;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public void deleteBookById(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }
        bookRepository.deleteById(bookId);
        log.info("Successfully deleted book with ID: {}", bookId);
    }

    public BookResponse getBookById(Long bookId) {
        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        log.info("Book retrieved: {}", book);

        return bookMapper.toResponse(book);
    }

    public Long createBook(BookRequest bookRequest) {
        String isbn = bookRequest.getIsbn();
        if (isbn != null && bookRepository.existsByIsbn(isbn)) {
            throw new IsbnAlreadyExistsException(isbn);
        }
        Book book = new Book().updateFromRequest(bookRequest);

        Long bookId = bookRepository.save(book).getId();
        log.info("Successfully created book with ID: {}", bookId);

        return bookId;
    }

    public PaginatedBookResponse getBooksPaginated(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findAll(pageable);
        log.info("Retrieved {} books", bookPage.getTotalElements());

        return bookMapper.toResponse(bookPage);
    }

    public Long updateBookById(Long bookId, BookRequest bookRequest) {
        String isbn = bookRequest.getIsbn();
        if (isbn != null && bookRepository.existsByIsbnAndIdNot(isbn, bookId)) {
            throw new IsbnAlreadyExistsException(isbn);
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId))
                .updateFromRequest(bookRequest);
        log.info("Successfully updated book with ID: {}", bookId);

        return bookRepository.save(book).getId();
    }
}
