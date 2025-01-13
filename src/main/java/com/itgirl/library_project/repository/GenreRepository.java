package com.itgirl.library_project.repository;

import com.itgirl.library_project.entity.Genre;

import java.util.List;

public interface GenreRepository<Genre, Long> {
    Object save(Genre genre);
   Genre getGenreById(Long id);
   Genre getGenreByName(String name);

    List<com.itgirl.library_project.entity.Genre> findAll();
}

