package com.itgirl.library_project.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class AuthorDto {
    private Long id;
    private String name;
    private String surname;
}
