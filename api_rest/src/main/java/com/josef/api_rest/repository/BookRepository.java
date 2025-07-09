package com.josef.api_rest.repository;

import com.josef.api_rest.model.Book;
import com.josef.api_rest.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
