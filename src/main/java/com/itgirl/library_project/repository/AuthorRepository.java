package com.itgirl.library_project.repository;

import com.itgirl.library_project.entity.Author;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    Author getAuthorById(Long id);

    Author getAllAuthorByName(String name);

    Optional<Author> findBySurnameAndName(String Surname, String Name);

    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books WHERE (:name IS NULL OR a.name = :name) AND (:surname IS NULL OR a.surname = :surname)")
    List<Author> findByNameOrSurname(@Param("name") String name, @Param("surname") String surname);

    Optional<Author> findByNameAndSurname(String name, String surname);
}



