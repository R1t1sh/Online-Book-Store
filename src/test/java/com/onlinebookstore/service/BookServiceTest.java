package com.onlinebookstore.service;

import com.onlinebookstore.entity.Book;
import com.onlinebookstore.exception.ResourceNotFoundException;
import com.onlinebookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;
    private final Long VALID_BOOK_ID = 1L;
    private final Long INVALID_BOOK_ID = 100L;
    private Book dummyBook;

    @BeforeEach
    void setUp() {

        dummyBook = new Book("Test Book", "Abhay", new BigDecimal("100"), LocalDate.of(2023, 1, 1));
        dummyBook.setId(VALID_BOOK_ID);
    }

    @Test
    void testAddBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(dummyBook);
        Book savedBook = bookService.addBook(dummyBook);

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        verify(bookRepository, times(1)).save(dummyBook);
    }

    @Test
    void testGetBookById_Found() {
        when(bookRepository.findById(VALID_BOOK_ID)).thenReturn(Optional.of(dummyBook));
        Book book = bookService.getBookById(VALID_BOOK_ID);

        assertNotNull(book);
        assertEquals(VALID_BOOK_ID, book.getId());
        verify(bookRepository, times(1)).findById(VALID_BOOK_ID);
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(INVALID_BOOK_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(INVALID_BOOK_ID));

        assertEquals("Book not found with ID: " + INVALID_BOOK_ID, exception.getMessage());
        verify(bookRepository, times(1)).findById(INVALID_BOOK_ID);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(dummyBook, new Book());
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }
}
