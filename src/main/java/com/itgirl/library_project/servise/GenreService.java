package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.Dto.GenreDto;
import com.itgirl.library_project.Exception.ResourceNotFoundException;
import com.itgirl.library_project.entity.Genre;
import com.itgirl.library_project.repository.GenreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public GenreDto getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
        return convertToDto(genre);
    }

    @Transactional
    public GenreDto updateGenre(Long id, GenreDto genreDto) {
        Genre existingGenre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
        modelMapper.map(genreDto, existingGenre);
        Genre updatedGenre = genreRepository.save(existingGenre);
        return modelMapper.map(updatedGenre, GenreDto.class);
    }

    private GenreDto convertToDto(Genre genre) {
        GenreDto genreDto = modelMapper.map(genre, GenreDto.class);

        List<BookDto> bookDtos = genre.getBooks().stream()
                .map(book -> {
                    BookDto bookDto = modelMapper.map(book, BookDto.class);
                    bookDto.setGenre(book.getGenre());

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

    @Transactional
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
        genreRepository.delete(genre);
    }
}


