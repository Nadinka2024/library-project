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
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

@Slf4j
@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public BookDto addNewBook(BookDto bookDto) {
        log.info("Получен запрос на добавление новой книги: {}", bookDto);

        if (bookDto.getName() == null || bookDto.getName().trim().isEmpty()) {
            log.error("Название книги не указано или пустое");
            throw new IllegalArgumentException("Название книги не может быть пустым");
        }

        Genre genre = genreRepository.findByName(String.valueOf(bookDto.getGenre()))
                .orElseGet(() -> {
                    Genre newGenre = new Genre();
                    newGenre.setName(String.valueOf(bookDto.getGenre()));
                    log.info("Создание нового жанра: {}", newGenre.getName());
                    return genreRepository.save(newGenre);
                });

        List<Author> authors = new ArrayList<>();
        for (AuthorDto authorDto : bookDto.getAuthors()) {
            Author author = authorRepository.findByNameAndSurname(authorDto.getName(), authorDto.getSurname())
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName(authorDto.getName());
                        newAuthor.setSurname(authorDto.getSurname());
                        log.info("Создание нового автора: {}", newAuthor.getName());
                        return authorRepository.save(newAuthor);
                    });
            authors.add(author);
        }

        Book book = modelMapper.map(bookDto, Book.class);
        book.setGenre(genre);
        book.setAuthors(authors);

        Book savedBook = bookRepository.save(book);
        log.info("Книга успешно добавлена: {}", savedBook);

        return modelMapper.map(savedBook, BookDto.class);
    }

    @Transactional
    public List<BookDto> getAllBooks() {
        log.info("Получение всех книг из базы данных");
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            log.warn("В базе данных нет книг.");
        }
        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDto getBookById(Long id) {
        log.info("Получение книги с ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Книга с ID: {} не найдена", id);
                    return new ResourceNotFoundException("Книга с ID " + id + " не найдена");
                });
        return modelMapper.map(book, BookDto.class);
    }

    @Transactional
    public BookDto updateBook(Long id, BookDto bookDto) {
        log.info("Запрос на обновление книги с ID: {}", id);
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга с ID " + id + " не найдена"));

        Genre genre = genreRepository.findByName(String.valueOf(bookDto.getGenre()))
                .orElseGet(() -> {
                    Genre newGenre = new Genre();
                    newGenre.setName(String.valueOf(bookDto.getGenre()));
                    log.info("Создание нового жанра для обновления: {}", newGenre.getName());
                    return genreRepository.save(newGenre);
                });

        List<Author> authors = new ArrayList<>();
        for (AuthorDto authorDto : bookDto.getAuthors()) {
            Author author = authorRepository.findByNameAndSurname(authorDto.getName(), authorDto.getSurname())
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName(authorDto.getName());
                        newAuthor.setSurname(authorDto.getSurname());
                        log.info("Создание нового автора для обновления: {}", newAuthor.getName());
                        return authorRepository.save(newAuthor);
                    });
            authors.add(author);
        }

        existingBook.setName(bookDto.getName());
        existingBook.setGenre(genre);
        existingBook.setAuthors(authors);

        Book updatedBook = bookRepository.save(existingBook);
        log.info("Книга успешно обновлена: {}", updatedBook);

        return modelMapper.map(updatedBook, BookDto.class);
    }

    @Transactional
    public void deleteBook(Long id) {
        log.info("Получен запрос на удаление книги с ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Книга с ID: {} не найдена", id);
                    return new ResourceNotFoundException("Книга с ID " + id + " не найдена");
                });
        bookRepository.delete(book);
        log.info("Книга с ID: {} успешно удалена", id);
    }
}