package com.onlinebookstore.controller;
import com.onlinebookstore.entity.Book;
import com.onlinebookstore.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookControllerTest {

    @Mock
    private BookService bookService;
    @InjectMocks
    private BookController bookController;
    private final Long VALID_BOOK_ID = 1L;
    private final Long INVALID_BOOK_ID = 100L;
    private Book dummyBook;

    @BeforeEach
    void setUp() {

        dummyBook = new Book("Dummy Book", "William", new BigDecimal("200"), LocalDate.of(2023, 1, 1));
        dummyBook.setId(VALID_BOOK_ID);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(dummyBook, new Book());
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertEquals(2, response.getBody().size());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById_Found() {
        when(bookService.getBookById(VALID_BOOK_ID)).thenReturn(dummyBook);

        ResponseEntity<Book> response = bookController.getBookById(VALID_BOOK_ID);

        assertNotNull(response.getBody());
        assertEquals("Dummy Book", response.getBody().getTitle());
        verify(bookService, times(1)).getBookById(VALID_BOOK_ID);
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookService.getBookById(INVALID_BOOK_ID)).thenThrow(new RuntimeException("Book not found with ID: " + INVALID_BOOK_ID));

        Exception exception = assertThrows(RuntimeException.class, () -> bookController.getBookById(INVALID_BOOK_ID));

        assertEquals("Book not found with ID: " + INVALID_BOOK_ID, exception.getMessage());
        verify(bookService, times(1)).getBookById(INVALID_BOOK_ID);
    }

    @Test
    void testAddBook() {
        when(bookService.addBook(any(Book.class))).thenReturn(dummyBook);

        ResponseEntity<Book> response = bookController.addBook(dummyBook);

        assertNotNull(response.getBody());
        assertEquals("Dummy Book", response.getBody().getTitle());
        verify(bookService, times(1)).addBook(any(Book.class));
    }
}
