package com.itgirl.library_project.controller;

import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.entity.Book;
import com.itgirl.library_project.servise.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;
    private Book book;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<Book> getAllBook() {
        return bookService.getAllBook();
    }

    @PostMapping("/books")
    public Book addNewBook(@RequestBody BookDto book){
        return bookService.addNewBook(book);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            Book book = bookService.getBookById(id);
            return ResponseEntity.ok(book);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the book");
        }
    }
}
