package com.josef.api_rest.repository;

import com.josef.api_rest.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query
    void disablePerson(@Param("id") Long id);
}
