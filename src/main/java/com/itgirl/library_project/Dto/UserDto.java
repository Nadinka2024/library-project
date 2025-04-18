package com.itgirl.library_project.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    @Size(min = 1, max = 10, message = "Размер имени должен быть от 1 до 10 знаков")
    @NotBlank(message = "Логин не может быть пустым")
    private String name;
    @NotBlank(message = "Email не может быть пустым")
    @Email
    private String email;
    @Min(value = 18, message = "Возраст не должен быть меньше 18")
    @Max(value = 120, message = "Возраст не должен быть больше 120")
    private int age;
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
    private String role;
}
