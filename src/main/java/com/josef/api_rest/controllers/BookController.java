package com.josef.api_rest.controllers;

import com.josef.api_rest.data.dto.v1.BookDTO;
import com.josef.api_rest.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/book")
public class BookController {

    @Autowired
    private BookService service;

    @GetMapping(value = "/v1/{id}")
    public BookDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping(value = "/v1")
    public List<BookDTO> findAll(){
        return service.findAll();
    }

    @PostMapping(value = "/v1")
    public BookDTO create(@RequestBody BookDTO bookDTO) {
        return service.create(bookDTO);
    }

    @PutMapping(value = "/v1")
    public BookDTO update(@RequestBody BookDTO bookDTO) {
        return service.update(bookDTO);
    }

    @DeleteMapping(value = "/v1/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
