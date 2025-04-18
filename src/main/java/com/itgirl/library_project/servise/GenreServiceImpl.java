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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public GenreDto addNewGenre(GenreDto genreDto) {
        try {
            log.info("Добавление нового жанра: {}", genreDto);
            Genre genre = modelMapper.map(genreDto, Genre.class);
            Genre savedGenre = genreRepository.save(genre);
            log.info("Жанр с ID {} успешно добавлен", savedGenre.getId());
            return modelMapper.map(savedGenre, GenreDto.class);
        } catch (Exception e) {
            log.error("Ошибка при добавлении жанра: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось добавить жанр");
        }
    }

    @Transactional(readOnly = true)
    public List<GenreDto> getAllGenres(String name) {
        try {
            log.info("Получение всех жанров (фильтр: {})", name);
            List<Genre> genres = genreRepository.findAll();
            return genres.stream()
                    .map(genre -> modelMapper.map(genre, GenreDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Ошибка при получении жанров: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось получить список жанров");
        }
    }

    @Transactional(readOnly = true)
    public GenreDto getGenreById(Long id) {
        try {
            log.info("Получение жанра по ID: {}", id);
            Genre genre = genreRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Жанр с ID " + id + " не найден"));
            return modelMapper.map(genre, GenreDto.class);
        } catch (ResourceNotFoundException e) {
            log.warn("Жанр не найден: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при получении жанра по ID: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось получить жанр");
        }
    }

    @Transactional
    public GenreDto updateGenre(Long id, GenreDto genreDto) {
        try {
            log.info("Обновление жанра с ID: {}", id);
            Genre existingGenre = genreRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Жанр с ID " + id + " не найден"));
            modelMapper.map(genreDto, existingGenre);
            Genre updatedGenre = genreRepository.save(existingGenre);
            log.info("Жанр обновлён: ID {}", updatedGenre.getId());
            return modelMapper.map(updatedGenre, GenreDto.class);
        } catch (ResourceNotFoundException e) {
            log.warn("Обновление не выполнено — жанр не найден: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при обновлении жанра: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось обновить жанр");
        }
    }

    @Transactional
    public void deleteGenre(Long id) {
        try {
            log.info("Удаление жанра с ID: {}", id);
            Genre genre = genreRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Жанр с ID " + id + " не найден"));
            genreRepository.delete(genre);
            log.info("Жанр с ID {} удалён", id);
        } catch (ResourceNotFoundException e) {
            log.warn("Удаление не выполнено — жанр не найден: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при удалении жанра: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось удалить жанр");
        }
    }

    @Transactional(readOnly = true)
    public List<GenreDto> getGenresByName(String name) {
        try {
            log.info("Поиск жанров по имени: {}", name);
            if (name == null || name.trim().isEmpty()) {
                return Collections.emptyList();
            }
            List<Genre> genres = genreRepository.findByNameContainingIgnoreCase(name);
            return genres.stream()
                    .map(genre -> modelMapper.map(genre, GenreDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Ошибка при поиске жанров по имени: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось выполнить поиск жанров по имени");
        }
    }
}