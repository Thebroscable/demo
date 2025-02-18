package com.example.demo.repository;

import com.example.demo.repository.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);

    @Query("SELECT a FROM book a WHERE " +
            "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.author) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.publisher) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.isbn) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    Page<Book> searchBooks(@Param("searchText") String searchText, Pageable pageable);
}
