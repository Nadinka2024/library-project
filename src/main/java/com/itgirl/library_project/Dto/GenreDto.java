package com.itgirl.library_project.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class GenreDto {
    private Long id;
    @NotBlank(message = "Название жанра не может быть пустым")
    private String name;
    private List<BookDto> books;
}