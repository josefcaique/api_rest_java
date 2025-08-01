package com.josef.api_rest.services;

import com.josef.api_rest.controllers.PersonController;
import com.josef.api_rest.controllers.TestLogController;
import com.josef.api_rest.data.dto.v1.PersonDTO;
import com.josef.api_rest.exception.FileStorageException;
import com.josef.api_rest.exception.RequiredObjectIsNullException;
import com.josef.api_rest.exception.ResourceNotFoundException;
import com.josef.api_rest.file.exporter.MediaTypes;
import com.josef.api_rest.file.exporter.contract.FileExporter;
import com.josef.api_rest.file.exporter.factory.FileExporterFactory;
import com.josef.api_rest.file.importer.contract.FileImporter;
import com.josef.api_rest.file.importer.factory.FileImporterFactory;
import com.josef.api_rest.mapper.custom.PersonMapper;
import com.josef.api_rest.model.Person;
import com.josef.api_rest.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import static com.josef.api_rest.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    FileImporterFactory importer;

    @Autowired
    FileExporterFactory exporter;


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
        return buildPagedModel(pageable, people);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable){
        logger.info("Finding people by name!");

        var people = repository.findPeopleByName(firstName, pageable);
        return buildPagedModel(pageable, people);
    }

    public Resource exportData(Pageable pageable, String acceptHeader){
        logger.info("Exporting a people page!");

        var people = repository.findAll(pageable)
                .map(person -> parseObject(person, PersonDTO.class))
                .getContent();
        try {
            FileExporter exporter = this.exporter.getExporter(acceptHeader);
            return exporter.exportFile(people);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export", e);
        }

    }


    public PersonDTO create(PersonDTO person) {

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one person!");
        var entity = parseObject(person, Person.class);
        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> dataCreation(MultipartFile file) {
        logger.info("Importing people from file");

        try(InputStream inputStream = file.getInputStream()) {
            String filename = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() ->  new BadRequestException("File name cannot be null"));
            FileImporter importer = this.importer.getImporter(filename);
            System.out.println(importer);

            List<Person> entities = importer.importFile(inputStream).stream()
                    .map(dto -> repository.save(parseObject(dto, Person.class)))
                    .toList();

            return entities.stream().map(entity -> {
                var dto = parseObject(entity,
                        PersonDTO.class);
                addHateoasLinks(dto);
                return dto;
            }).toList();


        } catch (Exception e) {
            throw new FileStorageException("Error processing the file!");
        }
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

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
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

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findByName("",1, 12, "asc")).withRel("findByName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class)).slash("dataCreation").withRel("dataCreation").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("UPDATE"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).exportPage(1, 12, "asc",
                null)).withRel("exportPage").withType("GET").withTitle("Export people"));
    }

}
