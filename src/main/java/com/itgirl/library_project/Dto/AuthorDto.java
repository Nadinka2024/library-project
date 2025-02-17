package com.itgirl.library_project.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthorDto {
    private Long id;
    @NotBlank(message = "Имя автора не может быть пустым")
    private String name;
    @NotBlank(message = "Фамилия автора не может быть пустой")
    private String surname;
}

