package com.itgirl.library_project.repository;

import com.itgirl.library_project.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author getAuthorById(Long id);
    Author getAllAuthorByName(String name);
}



