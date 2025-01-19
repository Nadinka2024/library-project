package com.itgirl.library_project.controller;

import com.itgirl.library_project.entity.Genre;
import com.itgirl.library_project.servise.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/genres")
    public Genre addNewGenre(@RequestBody Genre genre){
        return genreService.addNewGenre(genre);
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
