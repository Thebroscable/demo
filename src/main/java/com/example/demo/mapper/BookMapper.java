package com.example.demo.mapper;

import com.example.demo.models.BookResponse;
import com.example.demo.models.PaginatedBookResponse;
import com.example.demo.repository.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

    BookResponse toResponse(Book book);

    PaginatedBookResponse toResponse(Page<Book> bookPage);
}
