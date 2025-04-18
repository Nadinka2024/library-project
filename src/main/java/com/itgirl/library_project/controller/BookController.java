package com.itgirl.library_project.controller;

import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.Exception.ResourceNotFoundException;
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

@RestController
@RequestMapping("/books")
@AllArgsConstructor
@Slf4j
@Tag(name = "Книги", description = "Управление списком книг")
public class BookController {

    private final ModelMapper modelMapper;
    private final BookService bookService;

    @Operation(summary = "Добавление новой книги")
    @PostMapping
    public ResponseEntity<Object> addNewBook(@Valid @RequestBody BookDto bookDto, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Ошибка валидации при добавлении книги: {}", bookDto);
            Map<String, String> errors = new HashMap<>();
            result.getAllErrors().forEach(error -> {
                if (error instanceof FieldError fieldError) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
            });
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            log.info("Добавление новой книги: {}", bookDto);
            BookDto savedBook = bookService.addNewBook(bookDto);
            log.info("Книга успешно добавлена: {}", savedBook);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
        } catch (Exception e) {
            log.error("Ошибка при добавлении книги: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при добавлении книги");
        }
    }

    @Operation(summary = "Получение всех книг")
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        try {
            log.info("Запрос на получение всех книг");
            List<BookDto> books = bookService.getAllBooks();
            log.info("Найдено книг: {}", books.size());
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            log.error("Ошибка при получении списка книг: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Получение книги по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookById(@PathVariable Long id) {
        try {
            log.info("Запрос на получение книги по ID: {}", id);
            BookDto book = bookService.getBookById(id);
            return ResponseEntity.ok(book);
        } catch (ResourceNotFoundException e) {
            log.warn("Книга не найдена по ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка при получении книги: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при получении книги");
        }
    }

    @Operation(summary = "Обновление книги по ID")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        try {
            log.info("Запрос на обновление книги по ID: {}", id);
            BookDto updated = bookService.updateBook(id, bookDto);
            log.info("Книга успешно обновлена: {}", updated);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException e) {
            log.warn("Книга с ID {} не найдена для обновления", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка при обновлении книги: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обновлении книги");
        }
    }

    @Operation(summary = "Удаление книги по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        try {
            log.info("Запрос на удаление книги по ID: {}", id);
            bookService.deleteBook(id);
            log.info("Книга с ID {} успешно удалена", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.warn("Книга с ID {} не найдена для удаления", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка при удалении книги: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении книги");
        }
    }
}