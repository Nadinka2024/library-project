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

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        System.out.println("Get author with " + id);
        return authorRepository.getAuthorById(id);
    }

     public Author getAuthorByName(String authorSurname, String authorName) {
         System.out.println("Get author by name " + authorSurname+authorName);
         return null;
     }

    public List<Author> getAllAuthor() {
        return List.of();
    }
}

