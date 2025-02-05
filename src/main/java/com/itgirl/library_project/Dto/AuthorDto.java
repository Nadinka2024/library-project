package com.itgirl.library_project.Dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private List<BookDto> books;
}

