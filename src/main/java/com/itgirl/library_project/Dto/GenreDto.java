package com.itgirl.library_project.Dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GenreDto {
    private Long id;
    private String name;
    private List<BookDto> books;
}