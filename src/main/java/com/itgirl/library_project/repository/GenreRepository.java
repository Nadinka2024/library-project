package com.itgirl.library_project.repository;

import com.itgirl.library_project.entity.Genre;

import java.util.List;

public interface GenreRepository<Genre, Long> {
    Genre getGenreByGenreId(Long id);

    Genre getGenreByGenreName(String name);

    List<Genre> findAll();

    Object save(com.itgirl.library_project.entity.Genre genre);
}
