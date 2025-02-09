package com.itgirl.library_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.servise.AuthorService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto addNewAuthor(@RequestBody AuthorDto authorDto) {
        return authorService.addNewAuthor(authorDto);
    }

    @GetMapping
    public String getAllAuthors(@RequestParam(required = false) String name,
                                @RequestParam(required = false) String surname,
                                Model model) {
        List<AuthorDto> authorDtos = authorService.getAllAuthors(name, surname);

        System.out.println("Передаём в шаблон: " + authorDtos);

        model.addAttribute("authors", authorDtos);
        return "allAuthors";
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PutMapping("/{id}")
    public AuthorDto updateAuthor(@PathVariable Long id, @RequestBody AuthorDto authorDto) {
        return authorService.updateAuthor(id, authorDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    @GetMapping("/query")
    public List<AuthorDto> getAuthorsByNameOrSurname(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String surname) {
        return authorService.getAuthorsByNameOrSurname(name, surname);
    }

    @GetMapping("/criteria")
    public List<AuthorDto> getAuthorsByNameWithCriteria(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String surname) {
        if (StringUtils.isEmpty(name) && StringUtils.isEmpty(surname)) {
            return authorService.getAllAuthors(name, surname);
        } else {
            return authorService.getAuthorsByNameWithCriteria(name, surname);
        }
    }
}
