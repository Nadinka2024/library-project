package com.itgirl.library_project.repository;

import java.util.List;

public interface BookRepository<Book, Long> {
    List<Book> findAll();

    Book save(Book book);
    Book getBookById(Long id);
    Book getBookByName(String name);
}
