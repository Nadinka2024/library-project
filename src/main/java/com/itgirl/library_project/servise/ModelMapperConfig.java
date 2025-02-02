package com.itgirl.library_project.servise;

import com.itgirl.library_project.Exception.ResourceNotFoundException;
import com.itgirl.library_project.entity.Genre;
import com.itgirl.library_project.repository.GenreRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    private final GenreRepository genreRepository;

    @Autowired
    public ModelMapperConfig(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Конвертер для жанра (Genre) в строку (имя жанра)
        modelMapper.addConverter(new Converter<Genre, String>() {
            @Override
            public String convert(MappingContext<Genre, String> context) {
                Genre genre = context.getSource();
                return genre != null ? genre.getName() : null;  // Преобразуем жанр в его имя
            }
        });

        return modelMapper;
    }

}
