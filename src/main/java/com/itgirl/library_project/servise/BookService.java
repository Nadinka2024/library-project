package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto addNewBook(BookDto bookDto);

    List<BookDto> getAllBooks();

    BookDto getBookById(Long id);

    BookDto updateBook(Long id, BookDto bookDto);

    void deleteBook(Long id);
}
