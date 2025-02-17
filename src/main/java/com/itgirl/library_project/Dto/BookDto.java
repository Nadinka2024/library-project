package com.itgirl.library_project.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookDto {
    private Long id;
    @NotBlank(message = "Название книги не может быть пустым")
    private String name;
    private String genre;
    private List<AuthorDto> authors;
}
