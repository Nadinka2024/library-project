package com.itgirl.library_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.servise.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthorServiceImpl authorService;

    @InjectMocks
    private AuthorController authorController;

    private AuthorDto authorDto;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
        authorDto = AuthorDto.builder()
                .id(1L)
                .name("Test Author")
                .surname("Test Surname")
                .build();
    }

    @Test
    void addNewAuthor_shouldReturnStatusCreated() throws Exception {
        when(authorService.addNewAuthor(any(AuthorDto.class))).thenReturn(authorDto);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Author"))
                .andExpect(jsonPath("$.surname").value("Test Surname"));
    }

    @Test
    void getAllAuthors_shouldReturnAuthorList() throws Exception {
        when(authorService.getAllAuthors(null, null)).thenReturn(Collections.singletonList(authorDto));

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", Collections.singletonList(authorDto)));
    }

    @Test
    void getAuthorById_shouldReturnAuthor() throws Exception {
        when(authorService.getAuthorById(1L)).thenReturn(authorDto);

        mockMvc.perform(get("/authors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Author"))
                .andExpect(jsonPath("$.surname").value("Test Surname"));
    }

    @Test
    void updateAuthor_shouldReturnUpdatedAuthor() throws Exception {
        when(authorService.updateAuthor(any(Long.class), any(AuthorDto.class))).thenReturn(authorDto);

        mockMvc.perform(put("/authors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Author"))
                .andExpect(jsonPath("$.surname").value("Test Surname"));
    }

    @Test
    void deleteAuthor_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/authors/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}