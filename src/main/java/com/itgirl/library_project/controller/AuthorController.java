package com.itgirl.library_project.controller;
import com.itgirl.library_project.entity.Author;
import com.itgirl.library_project.servise.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class AuthorController {

    public AuthorService authorService;

    @Autowired
   public AuthorController (AuthorService authorService){
        this.authorService =authorService;
    }

    @GetMapping("/authors")
    public List<Author> getAllAuthors(@PathVariable Long id) {
        return authorService.getAllAuthor();
    }

    @GetMapping("/authors/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(authorService.getAuthorById(id));
        }catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author doesn't exist");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception while get author by id");
        }
    }
}
