package com.example.demo.repository.entity;

import com.example.demo.models.BookRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(nullable = false, length = 255)
    private String title;

    @Size(max = 255)
    @NotNull
    @Column(nullable = false, length = 255)
    private String author;

    @Size(max = 255)
    @NotNull
    @Column(nullable = false, length = 255)
    private String publisher;

    @Size(max = 13)
    @Column(unique = true, length = 13)
    private String isbn;

    private Integer pages;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Size(max = 50)
    @Column(length = 50)
    private String language;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Book updateFromRequest(BookRequest request) {
        this.title = request.getTitle();
        this.author = request.getAuthor();
        this.publisher = request.getPublisher();
        this.isbn = request.getIsbn();
        this.pages = request.getPages();
        this.releaseDate = request.getReleaseDate();
        this.language = request.getLanguage();
        this.description = request.getDescription();
        return this;
    }
}
