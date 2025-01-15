package com.itgirl.library_project.repository;

import com.itgirl.library_project.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Genre getGenreByGenreId(Long id);

    Genre getGenreByGenreName(String name);

    List<Genre> findAll();
}
