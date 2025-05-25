package com.josef.api_rest.services;

import com.josef.api_rest.controllers.TestLogController;
import com.josef.api_rest.data.dto.v1.BookDTO;
import com.josef.api_rest.exception.RequiredObjectIsNullException;
import com.josef.api_rest.exception.ResourceNotFoundException;
import com.josef.api_rest.model.Book;
import com.josef.api_rest.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.josef.api_rest.mapper.ObjectMapper.parseListObjects;
import static com.josef.api_rest.mapper.ObjectMapper.parseObject;

@Service
public class BookService {

    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName());

    @Autowired
    BookRepository repository;

    public BookDTO findById(Long id) {
        logger.info("Finding one book!");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return parseObject(entity, BookDTO.class);
    }

    public List<BookDTO> findAll() {
        logger.info("find all books!");
        return parseListObjects(repository.findAll(), BookDTO.class);
    }

    public BookDTO create(BookDTO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating a new book");
        var entity = parseObject(book, Book.class);
        var bookDto =  parseObject(repository.save(entity), BookDTO.class);
        return bookDto;
    }

    public BookDTO update(BookDTO bookDTO) {
        if (bookDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Updating a book!");
        BookDTO dto = findById(bookDTO.getId());
        dto.setAuthor(bookDTO.getAuthor());
        dto.setTitle(bookDTO.getTitle());
        dto.setPrice(bookDTO.getPrice());
        dto.setLaunchDate(bookDTO.getLaunchDate());

        var newDto = parseObject(repository.save(parseObject(dto, Book.class)), BookDTO.class);
        return newDto;
    }

    public void delete(Long id) {
        logger.info("Deleting a book!");
        BookDTO bookDto = findById(id);
        Book book = parseObject(bookDto, Book.class);
        repository.delete(book);
    }
}
