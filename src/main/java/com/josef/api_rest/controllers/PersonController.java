package com.josef.api_rest.controllers;

import com.josef.api_rest.controllers.docs.PersonControllerDocs;
import com.josef.api_rest.data.dto.v1.PersonDTO;
import com.josef.api_rest.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("api/person")
@Tag(name = "People", description = "Endpoits for Managing people")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonServices services;

    @GetMapping(value = "/v1/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Override
    public PersonDTO findById(@PathVariable("id") Long id){
        var person = services.findById(id);
        person.setBirthDay(new Date());
        person.setPhoneNumber("+55 (34) 98765-4321");
        person.setSensitiveData("Foo Bar");
        return person;
    }

    @GetMapping(value = "/v1", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Override
    public List<PersonDTO> findAll(){
        return services.findAll();
    }

    @PostMapping(value = "/v1", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Override
    public PersonDTO create(@RequestBody PersonDTO person) {
        return services.create(person);
    }

    @PutMapping(value = "/v1", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Override
    public PersonDTO update(@RequestBody PersonDTO person){
        return services.update(person);
    }

    @DeleteMapping(value = "/v1/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        services.delete(id);
        return ResponseEntity.noContent().build();
    }



}
