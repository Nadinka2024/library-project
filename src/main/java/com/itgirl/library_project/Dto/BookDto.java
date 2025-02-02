package com.itgirl.library_project.Dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookDto {
    private Long id;
    private String name;
    private String genre;
    private List<AuthorDto> authors;
}
