package com.itgirl.library_project.servise;


import com.itgirl.library_project.entity.Genre;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new Converter<Genre, String>() {
            @Override
            public String convert(MappingContext<Genre, String> context) {
                Genre genre = context.getSource();
                return genre != null ? genre.getName() : null;
            }
        });
        return modelMapper;
    }
}