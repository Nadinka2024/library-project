package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.GenreDto;
import com.itgirl.library_project.entity.Genre;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}


