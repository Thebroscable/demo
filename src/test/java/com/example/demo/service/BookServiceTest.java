package com.example.demo.service;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.exception.IsbnAlreadyExistsException;
import com.example.demo.models.BookRequest;
import com.example.demo.models.BookResponse;
import com.example.demo.models.PaginatedBookResponse;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteBookById_shouldDeleteBook_whenBookExists() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        bookService.deleteBookById(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void deleteBookById_shouldThrowException_whenBookDoesNotExist() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBookById(bookId));
    }

    @Test
    void getBookById_shouldReturnBook_whenBookExists() {
        Long bookId = 1L;
        Book book = Book.builder().id(bookId).build();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        BookResponse response = bookService.getBookById(bookId);

        assertNotNull(response);
        assertEquals(bookId, response.getId());
    }

    @Test
    void getBookById_shouldThrowException_whenBookDoesNotExist() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(bookId));
    }

    @Test
    void createBook_shouldThrowException_whenIsbnAlreadyExists() {
        BookRequest bookRequest = new BookRequest().isbn("9781234567897");
        when(bookRepository.existsByIsbn(bookRequest.getIsbn())).thenReturn(true);

        assertThrows(IsbnAlreadyExistsException.class, () -> bookService.createBook(bookRequest));
    }

    @Test
    void createBook_shouldSaveBook_whenIsbnDoesNotExist() {
        BookRequest bookRequest = new BookRequest().isbn("9781234567897");

        Book savedBook = Book.builder().id(1L).build();
        when(bookRepository.existsByIsbn(bookRequest.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        Long bookId = bookService.createBook(bookRequest);

        assertEquals(1L, bookId);
    }

    @Test
    void getBooksPaginated_shouldReturnPaginatedResponse() {
        Pageable pageable = PageRequest.of(0, 2);
        Book book1 = Book.builder().id(1L).build();
        Book book2 = Book.builder().id(2L).build();

        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book1, book2), pageable, 2);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        PaginatedBookResponse response = bookService.getBooksPaginated(0, 2);

        assertNotNull(response);
        assertEquals(2, response.getTotalElements());
    }

    @Test
    void updateBookById_shouldUpdateBook_whenIsbnIsUnique() {
        Long bookId = 1L;
        BookRequest bookRequest = new BookRequest().isbn("9781234567897");

        Book existingBook = Book.builder().id(bookId).build();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.existsByIsbnAndIdNot(bookRequest.getIsbn(), bookId)).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        Long updatedBookId = bookService.updateBookById(bookId, bookRequest);

        assertEquals(bookId, updatedBookId);
        assertEquals(existingBook.getIsbn(), bookRequest.getIsbn());
    }

    @Test
    void updateBookById_shouldThrowException_whenIsbnAlreadyExists() {
        Long bookId = 1L;
        BookRequest bookRequest = new BookRequest().isbn("9781234567897");

        when(bookRepository.existsByIsbnAndIdNot(bookRequest.getIsbn(), bookId)).thenReturn(true);

        assertThrows(IsbnAlreadyExistsException.class, () -> bookService.updateBookById(bookId, bookRequest));
    }
}
