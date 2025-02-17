package com.itgirl.library_project.controller;

import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.servise.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
@Slf4j
@Tag(name = "Книги", description = "Управление списком книг")
public class BookController {

    private final ModelMapper modelMapper;
    private final BookService bookService;

    @Operation(summary = "Добавление новых книг")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addNewBook(@Valid @RequestBody BookDto bookDto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getAllErrors().forEach(objectError -> {
                if (objectError instanceof FieldError fieldError) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
            });
            return ResponseEntity.badRequest().body(errors);
        }
        log.info("Добавление новой книги: {}", bookDto);
        BookDto savedBook = bookService.addNewBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @Operation(summary = "Получение списка всех книг", description = "Получаем список всех книг вместе с автором и жанром")
    @GetMapping
    public List<BookDto> getAllBooks() {
        log.info("Запрос на получение списка всех книг");
        List<BookDto> books = bookService.getAllBooks().stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
        log.info("Найдено {} книг", books.size());
        return books;
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@Valid @PathVariable Long id) {
        log.info("Получение книги по ID: {}", id);
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@Valid @PathVariable Long id, @RequestBody BookDto bookDto) {
        log.info("Измение книги по ID: {}", id);
        return bookService.updateBook(id, bookDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@Valid @PathVariable Long id) {
        log.info("Удаление книги по ID: {}", id);
        bookService.deleteBook(id);
    }
}
