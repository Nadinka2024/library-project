package com.itgirl.library_project.controller;
import com.itgirl.library_project.entity.Author;
import com.itgirl.library_project.servise.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();  // исправил на правильное имя метода
    }

    @GetMapping("/authors/{id}")
    public ResponseEntity<?> getAuthorByAuthorId(@PathVariable Long id) {
        try {
            Author author = authorService.getAuthorByAuthorId(id);
            return ResponseEntity.ok(author);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the author");
        }
    }
}
