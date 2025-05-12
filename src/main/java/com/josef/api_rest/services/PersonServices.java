package com.josef.api_rest.services;

import com.josef.api_rest.controllers.PersonController;
import com.josef.api_rest.controllers.TestLogController;
import com.josef.api_rest.data.dto.v1.PersonDTO;
import com.josef.api_rest.data.dto.v2.PersonDTOV2;
import com.josef.api_rest.exception.ResourceNotFoundException;
import com.josef.api_rest.mapper.custom.PersonMapper;
import com.josef.api_rest.model.Person;
import com.josef.api_rest.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;

import static com.josef.api_rest.mapper.ObjectMapper.parseListObjects;
import static com.josef.api_rest.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper converter;

    public PersonDTO findById(Long id){
        logger.info("Finding one person!");
        var entity =  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(id, dto);
        return dto;
    }

    public List<PersonDTO> findAll(){
        logger.info("Finding all people!");
        return parseListObjects(repository.findAll(), PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating one person!");
        var entity = parseObject(person, Person.class);
        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");
        PersonDTO person = findById(id);
        Person entity = parseObject(person, Person.class);
        repository.delete(entity);
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("updating person!");
        PersonDTO entity = findById(person.getId());

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return entity;
    }

    public PersonDTOV2 createV2(PersonDTOV2 person) {
        logger.info("Creating one person v2!");
        var entity = converter.convertDTO2Entity(person);
        return converter.convertEntity2DTO(repository.save(entity));
    }

    private static void addHateoasLinks(Long id, PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("UPDATE"));
        dto.add(linkTo(methodOn(PersonController.class).delete(id)).withRel("delete").withType("DELETE"));
    }
}
