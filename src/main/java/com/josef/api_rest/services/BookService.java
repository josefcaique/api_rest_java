package com.josef.api_rest.services;

import com.josef.api_rest.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

public class BookService {

    @Autowired
    BookRepository repository;


}
