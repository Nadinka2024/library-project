package com.itgirl.library_project.servise;

import com.itgirl.library_project.entity.Book;
import com.itgirl.library_project.repository.AuthorRepository;
import com.itgirl.library_project.repository.BookRepository;
import com.itgirl.library_project.repository.GenreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Book addNewBook(Book book) {
        log.info("Adding new book: {}", book.getName());
        try {
            return bookRepository.save(book);
        } catch (Exception e) {
            log.error("Error while saving book: {}", book.getName(), e);
            throw new RuntimeException("Failed to add new book", e);
        }
    }

    public List<Book> getAllBook() {
        log.info("Get all books");
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            log.error("Error while fetching all books", e);
            throw new RuntimeException("Failed to fetch all books", e);
        }
    }

    public Book getBookById(Long id) {
        log.info("Get book with id {}", id);
        try {
            return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        } catch (Exception e) {
            log.error("Error while fetching book with id {}", id, e);
            throw new RuntimeException("Failed to fetch book by ID", e);
        }
    }


    public Book getBookByName(String bookName) {
        log.info("Get book with name {}", bookName);
        try {
            return bookRepository.getBookByName(bookName);
        } catch (Exception e) {
            log.error("Error while fetching book with name {}", bookName, e);
            throw new RuntimeException("Failed to fetch book by name", e);
        }
    }
}
