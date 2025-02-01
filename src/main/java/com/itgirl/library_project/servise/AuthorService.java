package com.itgirl.library_project.servise;

import com.itgirl.library_project.Exception.AuthorNotFoundException;
import com.itgirl.library_project.entity.Author;
import com.itgirl.library_project.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//
//@Service
//@Slf4j
//
//public class AuthorService {
//
//    @Autowired
//    private AuthorRepository authorRepository;
//
//    public List<Author> findAll() {
//        return authorRepository.findAll();
//    }
//
//    public Author addNewAuthor(Author author) {
//        log.info("Adding new author: {}", author.getName());
//        return authorRepository.save(author);
//    }
//
//    public Author getAuthorById(Long id) {
//        System.out.println("Get author with ID " + id);
//        return authorRepository.findById(id)
//                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found"));
//    }
//
//    public Author getAllAuthorByName(String Surname, String Name) {
//        System.out.println("Get author by name " + Surname + " " + Name);
//        return authorRepository.findBySurnameAndName(Surname, Name)
//                .orElseThrow(() -> new AuthorNotFoundException("Author with name " + Name + " " + Surname + " not found"));
//    }
//
//    public List<Author> getAllAuthors() {
//        log.info("Get all authors with their books");
//        return authorRepository.findAll();
//    }
//
//    public void executeTask() {
//        System.out.println("Task executed by AuthorService.");
//    }
//}
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional


public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author addNewAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author getAuthorById(Long id) {
        return authorRepository.getAuthorById(id);
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
}
