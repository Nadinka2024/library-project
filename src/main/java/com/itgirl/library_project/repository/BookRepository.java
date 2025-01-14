package com.itgirl.library_project.repository;

import java.util.List;

public interface BookRepository<Book, Long> {
    Book getBookById(Long id);

    Book getBookByName(String name);

    List<com.itgirl.library_project.entity.Book> findAll();

    Object save(com.itgirl.library_project.entity.Book book);
}
