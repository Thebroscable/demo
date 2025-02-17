package com.example.demo.controller;

import com.example.demo.TestcontainersConfiguration;
import com.example.demo.models.BookRequest;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.entity.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    private Book savedBook;

    @BeforeEach
    public void setUp() {
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
    void shouldReturnBookById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/book/{bookId}", savedBook.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Great Gatsby"))
                .andExpect(jsonPath("$.author").value("F. Scott Fitzgerald"));
    }

    @Test
    void shouldReturnNotFoundForInvalidBookId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}", 99999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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

        mockMvc.perform(MockMvcRequestBuilders.post("/book/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void shouldReturnConflictForNonUniqueIsbn() throws Exception {
        BookRequest bookRequest = new BookRequest()
                .title("1984")
                .author("George Orwell")
                .publisher("Secker & Warburg")
                .isbn(savedBook.getIsbn());

        mockMvc.perform(MockMvcRequestBuilders.post("/book/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnBadRequestForInvalidInput() throws Exception {
        BookRequest bookRequest = new BookRequest()
                .title("1984")
                .author("George Orwell")
                .publisher("Secker & Warburg")
                .isbn("1");

        mockMvc.perform(MockMvcRequestBuilders.post("/book/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookRequest updatedBookRequest = new BookRequest()
                .title("1984")
                .author("George Orwell")
                .publisher("Secker & Warburg");

        mockMvc.perform(MockMvcRequestBuilders.put("/book/{bookId}", savedBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedBookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(savedBook.getId()));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/book/{bookId}", savedBook.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/book/{bookId}", savedBook.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBooksPaginated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/book/")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
