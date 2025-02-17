package com.example.demo.mapper;

import com.example.demo.models.BookResponse;
import com.example.demo.models.PaginatedBookResponse;
import com.example.demo.repository.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookResponse toResponse(Book book);

    PaginatedBookResponse toResponse(Page<Book> bookPage);
}
