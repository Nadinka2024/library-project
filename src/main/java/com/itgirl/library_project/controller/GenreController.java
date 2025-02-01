package com.itgirl.library_project.controller;

import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.Dto.GenreDto;
import com.itgirl.library_project.entity.Genre;
import com.itgirl.library_project.servise.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;
import java.util.stream.Collectors;

//@RestController
//public class GenreController {
//
//    private final GenreService genreService;
//
//    @Autowired
//    public GenreController(GenreService genreService) {
//        this.genreService = genreService;
//    }
//
//    @GetMapping("/genres")
//    public List<Genre> getAllGenre() {
//        return genreService.getAllGenre();
//    }
//
//    @PostMapping("/genres")
//    public Genre addNewGenre(@RequestBody Genre genre) {
//        return genreService.addNewGenre(genre);
//    }
//
//    @GetMapping("/genres/{id}")
//    public ResponseEntity<?> getGenreById(@PathVariable Long id) {
//        try {
//            Genre genre = genreService.getGenreById(id);
//            return ResponseEntity.ok(genre);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the genre");
//        }
//    }

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAllGenre() {
        return genreService.getAllGenre();
    }

    @PostMapping
    public Genre addNewGenre(@RequestBody Genre genre) {
        return genreService.addNewGenre(genre);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable Long id) {
        GenreDto genreDto = genreService.getGenreById(id);
        return ResponseEntity.ok(genreDto);
    }
}

