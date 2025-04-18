package com.itgirl.library_project.controller;

import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.Exception.ResourceNotFoundException;
import com.itgirl.library_project.servise.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookController bookController;

    private BookDto getSampleBook() {
        return BookDto.builder()
                .id(1L)
                .name("Война и мир")
                .genre("Роман")
                .authors(List.of(new AuthorDto(1L, "Лев", "Толстой")))
                .build();
    }

    @Test
    @DisplayName("addNewBook — успешное добавление")
    void addNewBook_success() {
        BookDto book = getSampleBook();
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(bookService.addNewBook(book)).thenReturn(book);

        ResponseEntity<Object> response = bookController.addNewBook(book, bindingResult);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(book, response.getBody());
    }

    @Test
    @DisplayName("addNewBook — ошибка валидации")
    void addNewBook_validationError() {
        BookDto book = getSampleBook();
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
                List.of(new FieldError("bookDto", "name", "Название обязательно"))
        );

        ResponseEntity<Object> response = bookController.addNewBook(book, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        Map<String, String> errors = (Map<String, String>) response.getBody();
        assertTrue(errors.containsKey("name"));
        assertEquals("Название обязательно", errors.get("name"));
    }

    @Test
    @DisplayName("getAllBooks — успех")
    void getAllBooks_success() {
        List<BookDto> books = List.of(getSampleBook());
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<BookDto>> response = bookController.getAllBooks();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(books, response.getBody());
    }

    @Test
    @DisplayName("getBookById — книга найдена")
    void getBookById_found() {
        BookDto book = getSampleBook();
        when(bookService.getBookById(1L)).thenReturn(book);

        ResponseEntity<Object> response = bookController.getBookById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(book, response.getBody());
    }

    @Test
    @DisplayName("getBookById — не найдена")
    void getBookById_notFound() {
        when(bookService.getBookById(99L)).thenThrow(new ResourceNotFoundException("Книга не найдена"));

        ResponseEntity<Object> response = bookController.getBookById(99L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Книга не найдена", response.getBody());
    }

    @Test
    @DisplayName("updateBook — успех")
    void updateBook_success() {
        BookDto updated = getSampleBook();
        when(bookService.updateBook(eq(1L), any(BookDto.class))).thenReturn(updated);

        ResponseEntity<Object> response = bookController.updateBook(1L, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updated, response.getBody());
    }

    @Test
    @DisplayName("updateBook — не найдена")
    void updateBook_notFound() {
        BookDto book = getSampleBook();
        when(bookService.updateBook(eq(1L), any())).thenThrow(new ResourceNotFoundException("Книга не найдена"));

        ResponseEntity<Object> response = bookController.updateBook(1L, book);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Книга не найдена", response.getBody());
    }

    @Test
    @DisplayName("deleteBook — успех")
    void deleteBook_success() {
        doNothing().when(bookService).deleteBook(1L);

        ResponseEntity<Object> response = bookController.deleteBook(1L);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("deleteBook — не найдена")
    void deleteBook_notFound() {
        doThrow(new ResourceNotFoundException("Книга не найдена")).when(bookService).deleteBook(99L);

        ResponseEntity<Object> response = bookController.deleteBook(99L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Книга не найдена", response.getBody());
    }
}