package com.itgirl.library_project.controller;

import com.itgirl.library_project.entity.Book;
import com.itgirl.library_project.servise.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<Book> getAllBook() {
        return bookService.getAllBook();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<?> getBookByBookId(@PathVariable Long id) {
        try {
            Book book = bookService.getBookByBookId(id);
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the book");
        }
    }
}
