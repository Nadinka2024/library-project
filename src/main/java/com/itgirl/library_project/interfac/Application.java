package com.itgirl.library_project.interfac;

import com.itgirl.library_project.servise.AuthorService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Application {
    private final AuthorServiceDto authorServiceDto;
    private final AuthorService authorService;

    @Autowired
    public Application(AuthorService authorService, AuthorServiceDto authorServiceDto){
        this.authorService = authorService;
        this.authorServiceDto = authorServiceDto;
    }
    @PostConstruct
    public void run (){
        authorServiceDto.performAction();
        authorService.executeTask();
    }
}
