package com.josef.api_rest.controllers;

import com.josef.api_rest.data.dto.v1.PersonDTO;
import com.josef.api_rest.data.dto.v2.PersonDTOV2;
import com.josef.api_rest.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonServices services;

    @GetMapping(value = "v1/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PersonDTO findById(@PathVariable("id") Long id){
        var person = services.findById(id);
        person.setBirthDay(new Date());
        person.setPhoneNumber("+55 (34) 98765-4321");
        person.setSensitiveData("Foo Bar");it
        return person;
    }

    @GetMapping(value="v1/", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<PersonDTO> findAll(){
        return services.findAll();
    }

    @PostMapping(value="v1/", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PersonDTO create(@RequestBody PersonDTO person) {
        return services.create(person);
    }

    @PutMapping(value="v1/", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PersonDTO update(@RequestBody PersonDTO person){
        return services.update(person);
    }

    @DeleteMapping(value="v1/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        services.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Controller to V2

    @PostMapping(value="/v2", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PersonDTOV2 create(@RequestBody PersonDTOV2 person) {
        return services.createV2(person);
    }


}
