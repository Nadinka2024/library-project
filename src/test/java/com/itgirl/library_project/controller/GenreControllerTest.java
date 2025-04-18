package com.itgirl.library_project.controller;

import com.itgirl.library_project.Dto.GenreDto;
import com.itgirl.library_project.Exception.ApiSuccessResponse;
import com.itgirl.library_project.servise.GenreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreControllerTest {

    @Mock
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;

    private GenreDto getSampleGenre() {
        return GenreDto.builder()
                .id(1L)
                .name("Фэнтези")
                .build();
    }

    @Test
    @DisplayName("addNewGenre — успех")
    void addNewGenre_success() {
        GenreDto genre = getSampleGenre();
        when(genreService.addNewGenre(genre)).thenReturn(genre);

        ResponseEntity<ApiSuccessResponse> response = genreController.addNewGenre(genre);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Жанр успешно добавлен", response.getBody().getMessage());
        assertEquals(genre, response.getBody().getData());
    }

    @Test
    @DisplayName("getAllGenres — без фильтра")
    void getAllGenres_noFilter() {
        List<GenreDto> genres = List.of(getSampleGenre());
        when(genreService.getAllGenres(null)).thenReturn(genres);

        ResponseEntity<ApiSuccessResponse> response = genreController.getAllGenres(null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Список жанров получен", response.getBody().getMessage());
        assertEquals(genres, response.getBody().getData());
    }

    @Test
    @DisplayName("getAllGenres — с фильтром по имени")
    void getAllGenres_withFilter() {
        List<GenreDto> genres = List.of(getSampleGenre());
        when(genreService.getGenresByName("Фэнтези")).thenReturn(genres);

        ResponseEntity<ApiSuccessResponse> response = genreController.getAllGenres("Фэнтези");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Список жанров получен", response.getBody().getMessage());
        assertEquals(genres, response.getBody().getData());
    }

    @Test
    @DisplayName("getGenreById — успех")
    void getGenreById_success() {
        GenreDto genre = getSampleGenre();
        when(genreService.getGenreById(1L)).thenReturn(genre);

        ResponseEntity<ApiSuccessResponse> response = genreController.getGenreById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Жанр найден", response.getBody().getMessage());
        assertEquals(genre, response.getBody().getData());
    }

    @Test
    @DisplayName("updateGenre — успех")
    void updateGenre_success() {
        GenreDto genre = getSampleGenre();
        when(genreService.updateGenre(1L, genre)).thenReturn(genre);

        ResponseEntity<ApiSuccessResponse> response = genreController.updateGenre(1L, genre);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Жанр успешно обновлён", response.getBody().getMessage());
        assertEquals(genre, response.getBody().getData());
    }

    @Test
    @DisplayName("deleteGenre — успех")
    void deleteGenre_success() {
        doNothing().when(genreService).deleteGenre(1L);

        ResponseEntity<ApiSuccessResponse> response = genreController.deleteGenre(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Жанр успешно удалён", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
