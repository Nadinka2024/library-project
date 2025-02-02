package com.itgirl.library_project.servise;

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

    public GenreDto addNewGenre(GenreDto genreDto) {
        if (genreDto.getName() == null || genreDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Genre name cannot be null or empty");
        }
        Genre genre = modelMapper.map(genreDto, Genre.class);
        Genre savedGenre = genreRepository.save(genre);
        return modelMapper.map(savedGenre, GenreDto.class);
    }

    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genre -> modelMapper.map(genre, GenreDto.class))
                .collect(Collectors.toList());
    }

    public GenreDto getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
        return modelMapper.map(genre, GenreDto.class);
    }

    @Transactional
    public GenreDto updateGenre(Long id, GenreDto genreDto) {
        log.info("Updating genre with id: {}", id);
        Genre existingGenre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
        modelMapper.map(genreDto, existingGenre);
        Genre updatedGenre = genreRepository.save(existingGenre);
        return modelMapper.map(updatedGenre, GenreDto.class);
    }

    private GenreDto convertToDto(Genre genre) {
        return modelMapper.map(genre, GenreDto.class);
    }

    @Transactional
    public void deleteGenre(Long id) {
        log.info("Deleting genre with id: {}", id);
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
        genreRepository.delete(genre);
        log.info("Genre with id {} deleted successfully", id);
    }
}
