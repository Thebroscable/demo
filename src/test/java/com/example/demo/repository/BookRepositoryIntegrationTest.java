package com.example.demo.repository;

import com.example.demo.TestcontainersConfiguration;
import com.example.demo.repository.entity.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class BookRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = bookRepository.save(Book.builder()
                .title("Book 1")
                .author("Author 1")
                .publisher("Publisher 1")
                .isbn("1234567890000")
                .build());

        book2 = bookRepository.save(Book.builder()
                .title("Book 2")
                .author("Author 2")
                .publisher("Publisher 1")
                .isbn("0987654321111")
                .build());
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldCheckIfBookExistsByIsbn() {
        boolean foundBook = bookRepository.existsByIsbn(book1.getIsbn());
        assertTrue(foundBook);
    }

    @Test
    void shouldReturnTrueWhenIsbnExistsWithDifferentId() {
        boolean exists = bookRepository.existsByIsbnAndIdNot(book1.getIsbn(), book2.getId());
        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenIsbnExistsWithSameId() {
        boolean exists = bookRepository.existsByIsbnAndIdNot(book1.getIsbn(), book1.getId());
        assertFalse(exists);
    }

    @Test
    void shouldReturnFalseWhenIsbnDoesNotExist() {
        boolean exists = bookRepository.existsByIsbnAndIdNot("1111111111111", book1.getId());
        assertFalse(exists);
    }
}
