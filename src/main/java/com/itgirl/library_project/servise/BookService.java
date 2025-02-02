package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.Exception.ResourceNotFoundException;
import com.itgirl.library_project.entity.Book;
import com.itgirl.library_project.entity.Genre;
import com.itgirl.library_project.repository.BookRepository;
import com.itgirl.library_project.repository.GenreRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.AllArgsConstructor;

import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public BookDto addNewBook(BookDto bookDto) {
        log.info("Adding new book with name: {}", bookDto.getName());
        Genre genre = genreRepository.findByName(bookDto.getGenre())
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with name " + bookDto.getGenre()));
        Book book = modelMapper.map(bookDto, Book.class);
        Book savedBook = bookRepository.save(book);
        log.info("Successfully added new book: {}", savedBook.getName());
        return modelMapper.map(savedBook, BookDto.class);
    }

    public List<BookDto> getAllBooks() {
        log.info("Fetching all books from the database");
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            log.warn("No books found in the database.");
        }
        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    public BookDto getBookById(Long id) {
        log.info("Fetching book with id: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found with id: {}", id);
                    return new ResourceNotFoundException("Book not found with id " + id);
                });
        return modelMapper.map(book, BookDto.class);
    }

    @Transactional
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        Genre genre = genreRepository.findByName(bookDto.getGenre())
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with name " + bookDto.getGenre()));
        modelMapper.map(bookDto, existingBook);
        existingBook.setGenre(genre);
        Book updatedBook = bookRepository.save(existingBook);
        return modelMapper.map(updatedBook, BookDto.class);
    }

    @Transactional
    public void deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        bookRepository.delete(book);
    }
}
