package com.itgirl.library_project.Dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenreDto {
    private Long id;
    private String genre;
    private List<BookDto> books;
}

