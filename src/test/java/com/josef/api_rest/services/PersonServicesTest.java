package com.josef.api_rest.services;

import com.josef.api_rest.data.dto.v1.PersonDTO;
import com.josef.api_rest.exception.RequiredObjectIsNullException;
import com.josef.api_rest.model.Person;
import com.josef.api_rest.repository.PersonRepository;
import com.josef.api_rest.unitetests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices services;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        var result = services.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("person/v1/1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("person/v1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("person/v1")
                        && Objects.equals(link.getType(), "POST"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("person/v1")
                        && Objects.equals(link.getType(), "UPDATE"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("person/v1/1")
                        && Objects.equals(link.getType(), "DELETE"))
        );

        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Address Test1", result.getAddress());
        assertEquals("Female", result.getGender());
    }

    @Test
    void create() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        PersonDTO dto = input.mockDTO(1);

        when(repository.save(person)).thenReturn(person);

        var result = services.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("person/v1/1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("person/v1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("person/v1")
                        && Objects.equals(link.getType(), "POST"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("person/v1")
                        && Objects.equals(link.getType(), "UPDATE"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("person/v1/1")
                        && Objects.equals(link.getType(), "DELETE"))
        );

        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Address Test1", result.getAddress());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testCreateWithNullPerson(){
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    services.create(null);
                });

        String exceptedMessage = "it is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(exceptedMessage));
    }

    @Test
    void delete() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        services.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Person.class));
        verifyNoInteractions(repository a);
    }

    @Test
    void update() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        PersonDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(person);

        var result = services.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("person/v1/1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("person/v1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("person/v1")
                        && Objects.equals(link.getType(), "POST"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("person/v1")
                        && Objects.equals(link.getType(), "UPDATE"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("person/v1/1")
                        && Objects.equals(link.getType(), "DELETE"))
        );

        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Address Test1", result.getAddress());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testUpdateWithNullPerson(){
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    services.create(null);
                });

        String exceptedMessage = "it is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(exceptedMessage));
    }

    @Test
    void findAll() {
    }
}