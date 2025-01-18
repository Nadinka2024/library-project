package com.itgirl.library_project.controller;

import com.itgirl.library_project.entity.Author;
import com.itgirl.library_project.entity.Genre;
import com.itgirl.library_project.servise.AuthorService;
import com.itgirl.library_project.servise.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenre() {
        return genreService.getAllGenre();
    }

    @GetMapping("/genres/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable Long id) {
        try {
            Genre genre = genreService.getGenreById(id);
            return ResponseEntity.ok(genre);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the genre");
        }
    }
}
