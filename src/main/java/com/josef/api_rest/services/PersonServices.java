package com.josef.api_rest.services;

import com.josef.api_rest.controllers.PersonController;
import com.josef.api_rest.controllers.TestLogController;
import com.josef.api_rest.data.dto.v1.PersonDTO;
import com.josef.api_rest.exception.RequiredObjectIsNullException;
import com.josef.api_rest.exception.ResourceNotFoundException;
import com.josef.api_rest.mapper.custom.PersonMapper;
import com.josef.api_rest.model.Person;
import com.josef.api_rest.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;

    public PersonDTO findById(Long id){
        logger.info("Finding one person!");
        var entity =  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable){
        logger.info("Finding all people!");

        var people = repository.findAll(pageable);

        var peopleWithLinks = people.map(person -> {
           var dto = parseObject(person, PersonDTO.class);
           addHateoasLinks(dto);
           return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PersonController.class)
                        .findAll(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                String.valueOf(pageable.getSort())))
                .withSelfRel();

        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    public PersonDTO create(PersonDTO person) {

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one person!");
        var entity = parseObject(person, Person.class);
        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling one person!");
        findById(id);
        repository.disablePerson(id);
        PersonDTO personDTO = findById(id);
        addHateoasLinks(personDTO);
        return personDTO;
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");
        PersonDTO person = findById(id);
        Person entity = parseObject(person, Person.class);
        addHateoasLinks(person);
        repository.delete(entity);
    }

    public PersonDTO update(PersonDTO personDTO) {

        if (personDTO == null) throw new RequiredObjectIsNullException();

        logger.info("updating person!");
        PersonDTO entity = findById(personDTO.getId());

        entity.setFirstName(personDTO.getFirstName());
        entity.setLastName(personDTO.getLastName());
        entity.setAddress(personDTO.getAddress());
        entity.setGender(personDTO.getGender());

        Person person = parseObject(entity, Person.class);
        repository.save(person);
        addHateoasLinks(entity);
        return entity;
    }



    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("UPDATE"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

}
