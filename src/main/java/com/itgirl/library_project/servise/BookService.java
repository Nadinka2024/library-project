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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public BookDto addNewBook(BookDto bookDto) {
        if (bookDto.getName() == null || bookDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Book name cannot be null or empty");
        }
        Genre genre = genreRepository.findByName(bookDto.getGenre())
                .orElseGet(() -> {
                    Genre newGenre = new Genre();
                    newGenre.setName(bookDto.getGenre());
                    return genreRepository.save(newGenre);
                });
        List<Author> authors = new ArrayList<>();
        for (AuthorDto authorDto : bookDto.getAuthors()) {
            Author author = authorRepository.findByNameAndSurname(authorDto.getName(), authorDto.getSurname())
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName(authorDto.getName());
                        newAuthor.setSurname(authorDto.getSurname());
                        return authorRepository.save(newAuthor);
                    });
            authors.add(author);
        }
        Book book = modelMapper.map(bookDto, Book.class);
        book.setGenre(genre);
        book.setAuthors(authors);
        Book savedBook = bookRepository.save(book);
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
                .orElseGet(() -> {
                    Genre newGenre = new Genre();
                    newGenre.setName(bookDto.getGenre());
                    return genreRepository.save(newGenre);
                });
        List<Author> authors = new ArrayList<>();
        for (AuthorDto authorDto : bookDto.getAuthors()) {
            Author author = authorRepository.findByNameAndSurname(authorDto.getName(), authorDto.getSurname())
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName(authorDto.getName());
                        newAuthor.setSurname(authorDto.getSurname());
                        return authorRepository.save(newAuthor);
                    });
            authors.add(author);
        }
        existingBook.setName(bookDto.getName());
        existingBook.setGenre(genre);
        existingBook.setAuthors(authors);
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
