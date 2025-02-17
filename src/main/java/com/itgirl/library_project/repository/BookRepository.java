package com.itgirl.library_project.repository;

import com.itgirl.library_project.entity.Book;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByName(String name);

    default Book getBookByName(String bookName) {
        return findByName(bookName)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with name: " + bookName));
    }
}


