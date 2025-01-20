package com.itgirl.library_project.repository;

import com.itgirl.library_project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findById(Long id);

    Optional<Book> findByName(String name);

    Book getBookByName(String bookName);
}
