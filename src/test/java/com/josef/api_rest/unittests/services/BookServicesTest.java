package com.josef.api_rest.unittests.services;

import com.josef.api_rest.data.dto.v1.BookDTO;
import com.josef.api_rest.exception.RequiredObjectIsNullException;
import com.josef.api_rest.integrationtests.testcontainers.AbstractIntegrationTest;
import com.josef.api_rest.model.Book;
import com.josef.api_rest.repository.BookRepository;
import com.josef.api_rest.services.BookService;
import com.josef.api_rest.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest extends AbstractIntegrationTest {

    MockBook input;

    @InjectMocks
    private BookService services;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        var result = services.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/book/v1/1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "POST"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "UPDATE"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/book/v1/1")
                        && Objects.equals(link.getType(), "DELETE"))
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(1.0, result.getPrice());
        assertEquals(new Date(2025,5,25), result.getLaunchDate());
    }

    @Test
    void create() {
        Book book = input.mockEntity(1);
        book.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.save(any(Book.class))).thenReturn(book);

        var result = services.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/book/v1/1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "POST"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "UPDATE"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/book/v1/1")
                        && Objects.equals(link.getType(), "DELETE"))
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(1.0, result.getPrice());
        assertEquals(new Date(2025,5,25), result.getLaunchDate());
    }

    @Test
    void testCreateWithNullBook(){
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
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        services.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Book.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void update() {
        Book book = input.mockEntity(1);
        book.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(any(Book.class))).thenReturn(book);

        var result = services.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/book/v1/1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "POST"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "UPDATE"))
        );

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/book/v1/1")
                        && Objects.equals(link.getType(), "DELETE"))
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(1.0, result.getPrice());
        assertEquals(new Date(2025,5,25), result.getLaunchDate());
    }

    @Test
    void testUpdateWithNullBook(){
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
        List<Book> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<BookDTO> book = services.findAll();

        assertNotNull(book);
        assertEquals(14, book.size());

        var book1 = book.get(1);

        assertNotNull(book1);
        assertNotNull(book1.getId());
        assertNotNull(book1.getLinks());
        assertTrue(book1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/book/v1/1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(book1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(book1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "POST"))
        );

        assertTrue(book1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "UPDATE"))
        );

        assertTrue(book1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/book/v1/1")
                        && Objects.equals(link.getType(), "DELETE"))
        );

        assertEquals("Author Test1", book1.getAuthor());
        assertEquals("Title Test1", book1.getTitle());
        assertEquals(1.0, book1.getPrice());
        assertEquals(new Date(2025,5,25), book1.getLaunchDate());

        var book2 = book.get(2);

        System.out.println(book2);
        assertNotNull(book2);
        assertNotNull(book2.getId());
        assertNotNull(book2.getLinks());
        assertTrue(book2.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/book/v1/2")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(book2.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "GET"))
        );

        assertTrue(book2.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "POST"))
        );

        assertTrue(book2.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/book/v1")
                        && Objects.equals(link.getType(), "UPDATE"))
        );

        assertTrue(book2.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/book/v1/2")
                        && Objects.equals(link.getType(), "DELETE"))
        );

        assertEquals("Author Test2", book2.getAuthor());
        assertEquals("Title Test2", book2.getTitle());
        assertEquals(2.0, book2.getPrice());
        assertEquals(new Date(2025,5,25), book2.getLaunchDate());
    }
}