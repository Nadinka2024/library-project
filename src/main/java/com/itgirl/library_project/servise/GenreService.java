package com.itgirl.library_project.servise;

import com.itgirl.library_project.entity.Genre;
import com.itgirl.library_project.repository.GenreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j

public class GenreService {

    @Autowired
    private GenreRepository genreRepository;


    public Genre addNewGenre(Genre genre) {
        return (Genre) genreRepository.save(genre);
    }

    public List<Genre> getAllGenre() {
        log.info("Get all genre");
        return genreRepository.findAll();
    }

    public Genre getGenreById(Long id) {
        System.out.println("Get genre with id " + id);
        return (Genre) genreRepository.getGenreById(id);
    }

    public Genre getGenreByName(String genre) {
        System.out.println("Get genre with name " + genre);
        return (Genre) genreRepository.getGenreByName(genre);
    }
}
