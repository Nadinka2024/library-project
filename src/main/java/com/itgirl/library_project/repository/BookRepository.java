package com.itgirl.library_project.repository;

import com.itgirl.library_project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface BookRepository extends JpaRepository<Book, Long> {
    Book getBookByBookId(Long id);

    Book getBookByName(String name);

    List<Book> findAll();

}
