package com.itgirl.library_project.interfac;

import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.Dto.BookDto;
import com.itgirl.library_project.entity.Author;
import com.itgirl.library_project.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorServiceDto {

    private final AuthorRepository authorRepository;

    @Override
    public void performAction() {
        System.out.println("Action performed by AuthorServiceDto");
    }

    @Override
    public AuthorDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow();
        return convertToDto(author);
    }

    private AuthorDto convertToDto(Author author) {
        List<BookDto> bookDtoList = author.getBooks()
                .stream()
                .map(book -> BookDto.builder()
                        .genre(book.getGenre().getName())
                        .name(book.getName())
                        .id(book.getId())
                        .build()
                ).toList();
        return AuthorDto.builder()
                .books((org.hibernate.mapping.List) bookDtoList)
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .build();
    }
}