package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.Exception.ResourceNotFoundException;
import com.itgirl.library_project.entity.Author;
import com.itgirl.library_project.entity.Book;
import com.itgirl.library_project.entity.Genre;
import com.itgirl.library_project.repository.AuthorRepository;
import com.itgirl.library_project.repository.BookRepository;
import com.itgirl.library_project.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    private BookRepository bookRepository;
    private GenreRepository genreRepository;
    private AuthorRepository authorRepository;
    private ModelMapper modelMapper;
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        genreRepository = mock(GenreRepository.class);
        authorRepository = mock(AuthorRepository.class);
        modelMapper = new ModelMapper();
        bookService = new BookServiceImpl(bookRepository, genreRepository, authorRepository, modelMapper);
    }

    @Test
    void addNewBook_Success() {
        BookDto bookDto = createSampleBookDto();

        Genre genre = new Genre();
        genre.setName("Novel");

        Author author = new Author();
        author.setName("Leo");
        author.setSurname("Tolstoy");

        Book book = new Book();
        book.setName("War and Peace");

        when(genreRepository.findByName("Novel")).thenReturn(Optional.of(genre));
        when(authorRepository.findByNameAndSurname("Leo", "Tolstoy")).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto result = bookService.addNewBook(bookDto);
        assertNotNull(result);
        assertEquals("War and Peace", result.getName());
    }

    @Test
    void addNewBook_EmptyName_ShouldThrowException() {
        BookDto bookDto = new BookDto();
        bookDto.setName("   "); // empty name
        assertThrows(IllegalArgumentException.class, () -> bookService.addNewBook(bookDto));
    }

    @Test
    void getAllBooks_ReturnsList() {
        Book book = new Book();
        book.setName("Anna Karenina");

        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDto> books = bookService.getAllBooks();
        assertEquals(1, books.size());
        assertEquals("Anna Karenina", books.get(0).getName());
    }

    @Test
    void getBookById_Success() {
        Book book = new Book();
        book.setId(1L);
        book.setName("Resurrection");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getBookById(1L);
        assertEquals("Resurrection", bookDto.getName());
    }

    @Test
    void getBookById_NotFound_ShouldThrow() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(999L));
    }

    @Test
    void updateBook_Success() {
        Long id = 1L;
        BookDto bookDto = createSampleBookDto();

        Book existingBook = new Book();
        existingBook.setId(id);
        existingBook.setName("Old Name");

        Genre genre = new Genre();
        genre.setName("Novel");

        Author author = new Author();
        author.setName("Leo");
        author.setSurname("Tolstoy");

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(genreRepository.findByName("Novel")).thenReturn(Optional.of(genre));
        when(authorRepository.findByNameAndSurname("Leo", "Tolstoy")).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        BookDto updatedBook = bookService.updateBook(id, bookDto);

        assertNotNull(updatedBook);
        assertEquals("War and Peace", updatedBook.getName());
    }

    @Test
    void updateBook_NotFound_ShouldThrow() {
        BookDto bookDto = createSampleBookDto();
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(1L, bookDto));
    }

    @Test
    void deleteBook_Success() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void deleteBook_NotFound_ShouldThrow() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(1L));
    }

    private BookDto createSampleBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setName("War and Peace");
        bookDto.setGenre("Novel");

        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("Leo");
        authorDto.setSurname("Tolstoy");

        bookDto.setAuthors(new ArrayList<>(Collections.singletonList(authorDto)));
        return bookDto;
    }
}
