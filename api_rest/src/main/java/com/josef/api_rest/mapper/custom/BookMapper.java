package com.josef.api_rest.mapper.custom;

import com.josef.api_rest.data.dto.v1.BookDTO;
import com.josef.api_rest.model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public BookDTO convertBookEntity2DTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setPrice(book.getPrice());
        bookDTO.setLaunchDate(book.getLaunchDate());
        return bookDTO;
    }

    public Book convertBookDTO2Entity(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setAuthor(bookDTO.getAuthor());
        book.setTitle(bookDTO.getTitle());
        book.setPrice(bookDTO.getPrice());
        book.setLaunchDate(bookDTO.getLaunchDate());
        return book;
    }
}
