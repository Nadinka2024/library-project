package com.itgirl.library_project.interfac;

import com.itgirl.library_project.Dto.AuthorDto;

public interface AuthorService {
    AuthorDto getAuthorById(Long id);
}