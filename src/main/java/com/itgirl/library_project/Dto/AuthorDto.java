package com.itgirl.library_project.Dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthorDto {
    private Long id;
    private String name;
    private String surname;
    private List<BookDto> books;
}

