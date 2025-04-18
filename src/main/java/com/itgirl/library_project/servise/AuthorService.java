package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.AuthorDto;

import java.util.List;

public interface AuthorService {

    AuthorDto addNewAuthor(AuthorDto authorDto);

    List<AuthorDto> getAllAuthors(String name, String surname);

    AuthorDto getAuthorById(Long id);

    AuthorDto updateAuthor(Long id, AuthorDto authorDto);

    void deleteAuthor(Long id);

    List<AuthorDto> getAuthorsByNameOrSurname(String name, String surname);

    Object getAuthorsByNameWithCriteria(String name, String surname);
}
