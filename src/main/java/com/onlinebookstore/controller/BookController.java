package com.onlinebookstore.controller;

import com.onlinebookstore.entity.Book;
import com.onlinebookstore.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/books")
@Validated
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        logger.info("Received request to add a book: {}", book.getTitle());
        try {
            Book savedBook = bookService.addBook(book);
            logger.info("Book added successfully with ID: {}", savedBook.getId());
            return ResponseEntity.ok(savedBook);
        } catch (Exception e) {
            logger.error("Failed to add book: {}", book.getTitle(), e);
            throw e;
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Book>> addMultipleBooks(@RequestBody List<Book> books) {
        logger.info("Received request to add {} books", books.size());
        try {
            List<Book> savedBooks = bookService.addMultipleBooks(books);
            logger.info("Successfully added {} books", savedBooks.size());
            return ResponseEntity.ok(savedBooks);
        } catch (Exception e) {
            logger.error("Failed to add multiple books", e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        logger.debug("Received request to fetch all books");
        try {
            List<Book> books = bookService.getAllBooks();
            logger.info("Fetched {} books successfully", books.size());
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            logger.error("Failed to fetch books", e);
            throw e;
        }
    }
//Commented this to prevent duplicate method name conflict
//    @GetMapping("/{id}")
//    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
//        logger.debug("Received request to fetch book with ID: {}", id);
//        try {
//            Book book = bookService.getBookById(id);
//            logger.info("Successfully fetched book with ID: {}", id);
//            return ResponseEntity.ok(book);
//        } catch (Exception e) {
//            logger.error("Failed to fetch book with ID: {}", id, e);
//            throw e;
//        }
//    }


    // Simulated change for Git demonstration
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        logger.info("Fetching book with ID: {}", id);
        return ResponseEntity.ok().body(bookService.getBookById(id));
    }



    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        logger.info("Received request to update book with ID: {}", id);
        try {
            Book updatedBook = bookService.updateBook(id, bookDetails);
            logger.info("Successfully updated book with ID: {}", id);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            logger.error("Failed to update book with ID: {}", id, e);
            throw e;
        }
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<String> deleteMultipleBooks(@RequestBody List<Long> bookIds) {
        logger.warn("Received request to delete multiple books with IDs: {}", bookIds);
        try {
            bookService.deleteMultipleBooks(bookIds);
            logger.info("Successfully deleted {} books", bookIds.size());
            return ResponseEntity.ok("Books deleted successfully.");
        } catch (Exception e) {
            logger.error("Failed to delete multiple books with IDs: {}", bookIds, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        logger.warn("Received request to delete book with ID: {}", id);
        try {
            bookService.deleteBook(id);
            logger.info("Successfully deleted book with ID: {}", id);
            return ResponseEntity.ok("Book deleted successfully.");
        } catch (Exception e) {
            logger.error("Failed to delete book with ID: {}", id, e);
            throw e;
        }
    }
}
