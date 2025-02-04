package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.Dto.GenreDto;
import com.itgirl.library_project.entity.Author;
import com.itgirl.library_project.entity.Book;
import com.itgirl.library_project.entity.Genre;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DtoMapper {

    public AuthorDto toAuthorDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .books(author.getBooks().stream()
                        .map(this::toBookDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public BookDto toBookDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .genre(book.getGenre().getName())
                .authors(book.getAuthors().stream()
                        .map(this::toAuthorDtoShort)
                        .collect(Collectors.toList()))
                .build();
    }

    public GenreDto toGenreDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .books(genre.getBooks().stream()
                        .map(this::toBookDtoShort)
                        .collect(Collectors.toList()))
                .build();
    }

    private AuthorDto toAuthorDtoShort(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .build();
    }

    private BookDto toBookDtoShort(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .build();
    }
}
