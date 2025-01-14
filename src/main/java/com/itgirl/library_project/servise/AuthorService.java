package com.itgirl.library_project.servise;

import com.itgirl.library_project.entity.Author;
import com.itgirl.library_project.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author getAuthorByAuthorId(Long id) {
        System.out.println("Get author with ID " + id);
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found"));
    }

    public Author getAllAuthorByName(String authorSurname, String authorName) {
        System.out.println("Get author by name " + authorSurname + " " + authorName);
        return authorRepository.findByAuthorSurnameAndAuthorName(authorSurname, authorName)
                .orElseThrow(() -> new AuthorNotFoundException("Author with name " + authorName + " " + authorSurname + " not found"));
    }

    public List<Author> getAllAuthors() {
        return findAll();
    }
}