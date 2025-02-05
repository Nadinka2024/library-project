package com.itgirl.library_project.Dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private List<AuthorDto> authors;
}
