package com.onlinebookstore.service;

import com.onlinebookstore.entity.Book;
import com.onlinebookstore.exception.ResourceNotFoundException;
import com.onlinebookstore.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private  BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        logger.info("Adding a new book: {}", book.getTitle());
        return bookRepository.save(book);
    }

    public List<Book> addMultipleBooks(List<Book> books) {
        logger.info("Adding multiple books. Count: {}", books.size());
        return bookRepository.saveAll(books);
    }

    public List<Book> getAllBooks() {
        logger.debug("Fetching all books...");
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        logger.debug("Fetching book with ID: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", id);
                    return new ResourceNotFoundException("Book not found with ID: " + id);
                });
    }

    public Book updateBook(Long id, Book bookDetails) {
        logger.info("Updating book with ID: {}", id);
        Book book = getBookById(id);
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPrice(bookDetails.getPrice());
        book.setPublishedDate(bookDetails.getPublishedDate());
        return bookRepository.save(book);
    }

    public void deleteMultipleBooks(List<Long> bookIds) {
        logger.warn("Deleting multiple books with IDs: {}", bookIds);
        List<Book> booksToDelete = bookRepository.findAllById(bookIds);
        if (booksToDelete.isEmpty()) {
            logger.error("No books found for given IDs: {}", bookIds);
            throw new ResourceNotFoundException("No books found for given IDs: " + bookIds);
        }
        bookRepository.deleteAll(booksToDelete);
    }

    public void deleteBook(Long id) {
        logger.warn("Deleting book with ID: {}", id);
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}
