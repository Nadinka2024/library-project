package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.GenreDto;

import java.util.List;

public interface GenreService {
    GenreDto addNewGenre(GenreDto genreDto);

    List<GenreDto> getAllGenres(String name);

    GenreDto getGenreById(Long id);

    GenreDto updateGenre(Long id, GenreDto genreDto);

    void deleteGenre(Long id);

    List<GenreDto> getGenresByName(String name);
}