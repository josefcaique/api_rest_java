package com.josef.api_rest.services;

import com.josef.api_rest.controllers.BookController;
import com.josef.api_rest.controllers.PersonController;
import com.josef.api_rest.controllers.TestLogController;
import com.josef.api_rest.controllers.docs.BookControllerDocs;
import com.josef.api_rest.data.dto.v1.BookDTO;
import com.josef.api_rest.data.dto.v1.PersonDTO;
import com.josef.api_rest.exception.RequiredObjectIsNullException;
import com.josef.api_rest.exception.ResourceNotFoundException;
import com.josef.api_rest.model.Book;
import com.josef.api_rest.repository.BookRepository;
import com.sun.codemodel.JForEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.josef.api_rest.mapper.ObjectMapper.parseListObjects;
import static com.josef.api_rest.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private final Logger logger = LoggerFactory.getLogger(TestLogController.class.getName());

    @Autowired
    BookRepository repository;

    public BookDTO findById(Long id) {
        logger.info("Finding one book!");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var bookDto = parseObject(entity, BookDTO.class);
        addHateoasLinks(bookDto);
        return bookDto;
    }

    public List<BookDTO> findAll() {
        logger.info("find all books!");
        var bookDtoList = parseListObjects(repository.findAll(), BookDTO.class);
        bookDtoList.forEach(this::addHateoasLinks);
        return bookDtoList;
    }

    public BookDTO create(BookDTO book) {

        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating a new book");
        var entity = parseObject(book, Book.class);
        var bookDto =  parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(bookDto);
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
        addHateoasLinks(newDto);
        return newDto;
    }

    public void delete(Long id) {
        logger.info("Deleting a book!");
        BookDTO bookDto = findById(id);
        Book book = parseObject(bookDto, Book.class);
        addHateoasLinks(bookDto);
        repository.delete(book);
    }

    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("UPDATE"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
