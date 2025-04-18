package com.itgirl.library_project.controller;

import com.itgirl.library_project.Exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.servise.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authors")
@AllArgsConstructor
@Slf4j
@Tag(name = "Авторы", description = "Управление авторами")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Добавление нового автора")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Автор успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PostMapping
    public ResponseEntity<AuthorDto> addNewAuthor(@Valid @RequestBody AuthorDto authorDto) {
        try {
            log.info("Запрос на добавление нового автора: {}", authorDto);
            AuthorDto createdAuthor = authorService.addNewAuthor(authorDto);
            log.info("Автор успешно добавлен с ID: {}", createdAuthor.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
        } catch (IllegalArgumentException e) {
            log.error("Ошибка при добавлении автора: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new AuthorDto());
        } catch (Exception e) {
            log.error("Неизвестная ошибка при добавлении автора: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((AuthorDto) Map.of("message", "Произошла ошибка при добавлении автора"));
        }
    }

    @Operation(summary = "Получение списка всех авторов")
    @GetMapping
    public String getAllAuthors(@Valid @RequestParam(required = false) String name,
                                @RequestParam(required = false) String surname,
                                Model model) {
        try {
            log.info("Получение списка авторов с фильтром: name={}, surname={}", name, surname);
            List<AuthorDto> authorDtos = authorService.getAllAuthors(name, surname);
            if (authorDtos.isEmpty()) {
                log.info("Не найдено авторов по заданным фильтрам");
            }
            model.addAttribute("authors", authorDtos);
            return "allAuthors";
        } catch (Exception e) {
            log.error("Ошибка при получении списка авторов: {}", e.getMessage(), e);
            model.addAttribute("error", "Произошла ошибка при получении списка авторов.");
            return "error";
        }
    }

    @Operation(summary = "Получение автора по Id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorById(@Valid @PathVariable Long id) {
        try {
            log.info("Получение автора по ID: {}", id);
            AuthorDto authorDto = authorService.getAuthorById(id);
            return ResponseEntity.ok(authorDto);
        } catch (ResourceNotFoundException e) {
            log.error("Автор с ID {} не найден", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Автор с ID " + id + " не существует"));
        } catch (Exception e) {
            log.error("Ошибка при получении автора по ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Произошла ошибка при получении данных"));
        }
    }

    @Operation(summary = "Обновление автора по Id")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@Valid @PathVariable Long id, @RequestBody AuthorDto authorDto) {
        try {
            log.info("Обновление автора с ID: {}", id);
            AuthorDto updatedAuthor = authorService.updateAuthor(id, authorDto);
            log.info("Автор с ID {} успешно обновлен", updatedAuthor.getId());
            return ResponseEntity.ok(updatedAuthor);
        } catch (ResourceNotFoundException e) {
            log.error("Автор с ID {} не найден для обновления", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthorDto());
        } catch (Exception e) {
            log.error("Ошибка при обновлении автора с ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((AuthorDto) Map.of("message", "Произошла ошибка при обновлении данных"));
        }
    }

    @Operation(summary = "Удаление автора по Id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAuthor(@Valid @PathVariable Long id) {
        try {
            log.info("Удаление автора с ID: {}", id);
            authorService.deleteAuthor(id);
            log.info("Автор с ID {} успешно удален", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.error("Автор с ID {} не найден для удаления", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Ошибка при удалении автора с ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Получение авторов по имени или фамилии")
    @GetMapping("/query")
    public ResponseEntity<?> getAuthorsByNameOrSurname(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname) {
        try {
            log.info("Получение авторов по имени или фамилии: name={}, surname={}", name, surname);

            List<AuthorDto> authors = authorService.getAuthorsByNameOrSurname(name, surname);

            if (authors.isEmpty()) {
                String errorMessage = (name != null ? "Автор с именем '" + name + "' " : "")
                        + (surname != null ? "и фамилией '" + surname + "'" : "") + "не найден";
                log.info(errorMessage);

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", errorMessage));
            }

            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            log.error("Ошибка при получении авторов по имени или фамилии", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Произошла ошибка при получении данных"));
        }
    }

    @Operation(summary = "Получение авторов с использованием критериев")
    @GetMapping("/criteria")
    public ResponseEntity<?> getAuthorsByNameWithCriteria(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname) {
        try {
            log.info("Получение авторов с использованием критериев: name={}, surname={}", name, surname);
            List<AuthorDto> authors = (List<AuthorDto>) authorService.getAuthorsByNameWithCriteria(name, surname);

            if (authors.isEmpty()) {
                String errorMessage = (name != null ? "Автор с таким именем '" + name + "' " : "")
                        + (surname != null ? "и фамилией '" + surname + "'" : "") + "не найден";
                log.info(errorMessage);

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", errorMessage));
            }

            return ResponseEntity.ok(authors);
        } catch (Exception e) {
            log.error("Ошибка при получении авторов с критериями", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Произошла ошибка при получении данных"));
        }
    }
}