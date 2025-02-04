package com.itgirl.library_project.Dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookDto {
    private Long id;
    private String name;
    private String genre;
    private List<AuthorDto> authors;
}
