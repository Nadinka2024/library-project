package com.itgirl.library_project.controller;

import com.itgirl.library_project.Dto.GenreDto;
import com.itgirl.library_project.Exception.ApiSuccessResponse;
import com.itgirl.library_project.servise.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
@Tag(name = "Жанры книг", description = "Управление жанрами в библиотеке")
public class GenreController {

    private final GenreService genreService;

    @Operation(summary = "Добавить новый жанр")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Жанр успешно добавлен",
                    content = @Content(schema = @Schema(implementation = GenreDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ApiSuccessResponse> addNewGenre(@Valid @RequestBody GenreDto genreDto) {
        log.info("POST /genres — добавление жанра: {}", genreDto);
        GenreDto saved = genreService.addNewGenre(genreDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse("Жанр успешно добавлен", saved));
    }

    @Operation(summary = "Получить все жанры")
    @GetMapping
    public ResponseEntity<ApiSuccessResponse> getAllGenres(@RequestParam(value = "name", required = false) String name) {
        log.info("GET /genres — получение жанров, фильтр: '{}'", name);
        List<GenreDto> genres = (name == null || name.isBlank())
                ? genreService.getAllGenres(null)
                : genreService.getGenresByName(name);
        return ResponseEntity.ok(new ApiSuccessResponse("Список жанров получен", genres));
    }

    @Operation(summary = "Получить жанр по ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse> getGenreById(@PathVariable Long id) {
        log.info("GET /genres/{} — получение жанра по ID", id);
        GenreDto genre = genreService.getGenreById(id);
        return ResponseEntity.ok(new ApiSuccessResponse("Жанр найден", genre));
    }

    @Operation(summary = "Обновить жанр по ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse> updateGenre(@PathVariable Long id, @Valid @RequestBody GenreDto genreDto) {
        log.info("PUT /genres/{} — обновление жанра: {}", id, genreDto);
        GenreDto updated = genreService.updateGenre(id, genreDto);
        return ResponseEntity.ok(new ApiSuccessResponse("Жанр успешно обновлён", updated));
    }

    @Operation(summary = "Удалить жанр по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse> deleteGenre(@PathVariable Long id) {
        log.info("DELETE /genres/{} — удаление жанра", id);
        genreService.deleteGenre(id);
        return ResponseEntity.ok(new ApiSuccessResponse("Жанр успешно удалён", null));
    }
}