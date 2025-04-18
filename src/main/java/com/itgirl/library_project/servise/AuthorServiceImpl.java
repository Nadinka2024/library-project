package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.Exception.ResourceNotFoundException;
import com.itgirl.library_project.Specification.AuthorSpecification;
import com.itgirl.library_project.entity.Author;
import com.itgirl.library_project.repository.AuthorRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public AuthorDto addNewAuthor(AuthorDto authorDto) {
        try {
            log.info("Добавление нового автора: {}", authorDto);
            Author author = modelMapper.map(authorDto, Author.class);
            Author savedAuthor = authorRepository.save(author);
            log.info("Автор с ID {} успешно добавлен", savedAuthor.getId());
            return modelMapper.map(savedAuthor, AuthorDto.class);
        } catch (Exception e) {
            log.error("Ошибка при добавлении автора: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при добавлении автора");
        }
    }

    @Transactional
    public List<AuthorDto> getAllAuthors(String name, String surname) {
        try {
            log.info("Запрос на получение всех авторов с фильтром: name={}, surname={}", name, surname);
            List<Author> authors = authorRepository.findAll();
            log.info("Найдено {} авторов", authors.size());
            return authors.stream()
                    .map(author -> modelMapper.map(author, AuthorDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Ошибка при получении списка авторов: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении списка авторов");
        }
    }

    @Transactional
    public AuthorDto getAuthorById(Long id) {
        try {
            log.info("Запрос на получение автора с ID: {}", id);
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Автор с ID {} не найден", id);
                        return new ResourceNotFoundException("Author not found with id " + id);
                    });
            return modelMapper.map(author, AuthorDto.class);
        } catch (ResourceNotFoundException e) {
            log.error("Автор с ID {} не найден", id);
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при получении автора с ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении автора");
        }
    }

    @Transactional
    public AuthorDto updateAuthor(Long id, AuthorDto authorDto) {
        try {
            log.info("Запрос на обновление автора с ID: {}", id);
            Author existingAuthor = authorRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Автор с ID {} не найден", id);
                        return new ResourceNotFoundException("Author not found with id " + id);
                    });

            modelMapper.map(authorDto, existingAuthor);

            Author updatedAuthor = authorRepository.save(existingAuthor);

            log.info("Автор с ID {} успешно обновлен", updatedAuthor.getId());

            return modelMapper.map(updatedAuthor, AuthorDto.class);

        } catch (ResourceNotFoundException e) {
            log.error("Автор с ID {} не найден для обновления", id);
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при обновлении автора с ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Ошибка при обновлении автора");
        }
    }

    @Transactional
    public void deleteAuthor(Long id) {
        try {
            log.info("Запрос на удаление автора с ID: {}", id);
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Автор с ID {} не найден", id);
                        return new ResourceNotFoundException("Author not found with id " + id);
                    });
            authorRepository.delete(author);
            log.info("Автор с ID {} успешно удален", id);
        } catch (ResourceNotFoundException e) {
            log.error("Автор с ID {} не найден для удаления", id);
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при удалении автора с ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Ошибка при удалении автора");
        }
    }

    @Transactional
    public List<AuthorDto> getAuthorsByNameOrSurname(String name, String surname) {
        try {
            List<Author> authors = authorRepository.findByNameOrSurname(name, surname);
            if (authors.isEmpty()) {
                log.info("Не найдено авторов с именем: {} и фамилией: {}", name, surname);
                return Collections.emptyList();
            }
            return authors.stream()
                    .map(author -> modelMapper.map(author, AuthorDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Ошибка при получении авторов по имени или фамилии", e);
            throw new RuntimeException("Ошибка при получении авторов по имени или фамилии", e);
        }
    }

    @Transactional
    public List<AuthorDto> getAuthorsByNameWithCriteria(String name, String surname) {
        try {
            log.info("Запрос на получение авторов с использованием критериев: name={}, surname={}", name, surname);
            Specification<Author> specification = Specification.where(null);

            if (!StringUtils.isEmpty(name)) {
                specification = specification.and(AuthorSpecification.hasName(name));
            }
            if (!StringUtils.isEmpty(surname)) {
                specification = specification.and(AuthorSpecification.hasSurname(surname));
            }

            List<Author> authors = authorRepository.findAll(specification);
            log.info("Найдено {} авторов по критериям", authors.size());

            return authors.stream()
                    .map(author -> modelMapper.map(author, AuthorDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Ошибка при получении авторов с использованием критериев", e);
            throw new RuntimeException("Ошибка при получении авторов с критериями");
        }
    }
}