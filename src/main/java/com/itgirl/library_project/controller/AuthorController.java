package com.itgirl.library_project.controller;

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
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto addNewAuthor(@Valid @RequestBody AuthorDto authorDto) {
        log.info("Добавление автора книги: {}", authorDto);
        return authorService.addNewAuthor(authorDto);
    }

    @GetMapping
    public String getAllAuthors(@Valid @RequestParam(required = false) String name,
                                @RequestParam(required = false) String surname,
                                Model model) {
        log.info("Получение списка авторов");
        List<AuthorDto> authorDtos = authorService.getAllAuthors(name, surname);
        model.addAttribute("authors", authorDtos);
        return "allAuthors";
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@Valid @PathVariable Long id) {
        log.info("Получение автора по ID: {}", id);
        return authorService.getAuthorById(id);
    }

    @PutMapping("/{id}")
    public AuthorDto updateAuthor(@Valid @PathVariable Long id, @RequestBody AuthorDto authorDto) {
        log.info("Измение автора по ID: {}", id);
        return authorService.updateAuthor(id, authorDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@Valid @PathVariable Long id) {
        log.info("Удаление автора по ID: {}", id);
        authorService.deleteAuthor(id);
    }

    @GetMapping("/query")
    public List<AuthorDto> getAuthorsByNameOrSurname(@Valid @RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String surname) {
        return authorService.getAuthorsByNameOrSurname(name, surname);
    }

    @GetMapping("/criteria")
    public ResponseEntity<?> getAuthorsByNameWithCriteria(@RequestParam(required = false) String name,
                                                          @RequestParam(required = false) String surname) {
        try {
            if ((name == null || name.isBlank()) && (surname == null || surname.isBlank())) {
                return ResponseEntity.ok(authorService.getAllAuthors(name, surname));
            }
            return ResponseEntity.ok(authorService.getAuthorsByNameWithCriteria(name, surname));
        } catch (Exception e) {
            log.error("Ошибка при получении авторов: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
