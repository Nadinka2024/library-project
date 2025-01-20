package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.entity.Book;
import com.itgirl.library_project.entity.Genre;
import com.itgirl.library_project.repository.AuthorRepository;
import com.itgirl.library_project.repository.BookRepository;
import com.itgirl.library_project.repository.GenreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Book addNewBook(BookDto bookDto) {
        Genre genre = genreRepository.findById(bookDto.getGenreId())
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        Book book = new Book();
        book.setName(bookDto.getName());
        book.setGenre(genre);
        return bookRepository.save(book);
    }

    public Book getBookById(Long id) {
        log.info("Get book with id {}", id);
        try {
            return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        } catch (Exception e) {
            log.error("Error while fetching book with id {}", id, e);
            throw new RuntimeException("Failed to fetch book by ID", e);
        }
    }


    public Book getBookByName(String bookName) {
        log.info("Get book with name {}", bookName);
        try {
            return bookRepository.getBookByName(bookName);
        } catch (Exception e) {
            log.error("Error while fetching book with name {}", bookName, e);
            throw new RuntimeException("Failed to fetch book by name", e);
        }
    }

    public List<Book> getAllBook() {
        log.info("Fetching all books...");
        return bookRepository.findAll();
    }
}
