package com.itgirl.library_project.servise;


import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.Dto.GenreDto;
import com.itgirl.library_project.entity.Author;
import com.itgirl.library_project.entity.Genre;
import com.itgirl.library_project.repository.GenreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    public Genre addNewGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public List<Genre> getAllGenre() {
        return genreRepository.findAll();
    }

    public GenreDto getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
        return convertToDto(genre);
    }

    private GenreDto convertToDto(Genre genre) {
        GenreDto genreDto = modelMapper.map(genre, GenreDto.class);
        List<BookDto> bookDtos = genre.getBooks().stream()
                .map(book -> {
                    BookDto bookDto = modelMapper.map(book, BookDto.class);
                    List<AuthorDto> authorDtos = book.getAuthors().stream()
                            .map(author -> modelMapper.map(author, AuthorDto.class))
                            .collect(Collectors.toList());
                    bookDto.setAuthors(authorDtos);
                    return bookDto;
                })
                .collect(Collectors.toList());
        genreDto.setBooks(bookDtos);
        return genreDto;
    }
}

