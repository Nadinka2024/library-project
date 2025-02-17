package com.itgirl.library_project.controller;

import com.itgirl.library_project.Dto.GenreDto;
import com.itgirl.library_project.servise.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/genres")
@AllArgsConstructor
@Tag(name = "Жанры книг", description = "Управление списком жанров")
public class GenreController {

    private final GenreService genreService;

    @Operation(summary = "Добавить новый жанр")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Жанр успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<Object> addNewGenre(@Valid @RequestBody GenreDto genreDto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getAllErrors().forEach(objectError -> {
                if (objectError instanceof FieldError fieldError) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
            });
            return ResponseEntity.badRequest().body(errors);
        }
        log.info("Добавление нового жанра: {}", genreDto);
        GenreDto savedGenre = genreService.addNewGenre(genreDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGenre);
    }


    @Operation(summary = "Получить все жанры")
    @GetMapping
    public List<GenreDto> getAllGenres() {
        log.info("Получение списка всех жанров");
        return genreService.getAllGenres();
    }

    @Operation(summary = "Получить жанр по ID")
    @GetMapping("/{id}")
    public GenreDto getGenreById(@Valid @PathVariable Long id) {
        log.info("Получение жанра по ID: {}", id);
        return genreService.getGenreById(id);
    }

    @PutMapping("/{id}")
    public GenreDto updateGenre(@Valid @PathVariable Long id, @RequestBody GenreDto genreDto) {
        log.info("Изменение жанра по ID: {}", id);
        return genreService.updateGenre(id, genreDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(@PathVariable Long id) {
        log.info("Удаление жанра по ID: {}", id);
        genreService.deleteGenre(id);
    }
}



