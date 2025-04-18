package com.itgirl.library_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.Dto.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        AuthorDto author = AuthorDto.builder()
                .name("Лев")
                .surname("Толстой")
                .build();
        bookDto = BookDto.builder()
                .name("Война и мир " + UUID.randomUUID())
                .genre("Роман")
                .authors(List.of(author))
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addNewBook_shouldReturnCreated() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", containsString("Война и мир")))
                .andExpect(jsonPath("$.genre").value("Роман"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllBooks_shouldReturnList() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getBookById_shouldReturnBook() throws Exception {
        String response = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BookDto created = objectMapper.readValue(response, BookDto.class);

        mockMvc.perform(get("/books/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", containsString("Война и мир")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getBookById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/books/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("не найдена")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateBook_shouldReturnUpdated() throws Exception {
        String response = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BookDto created = objectMapper.readValue(response, BookDto.class);
        created.setGenre("Исторический");

        mockMvc.perform(put("/books/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genre").value("Исторический"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateBook_shouldReturnNotFound() throws Exception {
        bookDto.setGenre("Фантастика");

        mockMvc.perform(put("/books/{id}", 12345L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("не найдена")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBook_shouldReturnNoContent() throws Exception {
        String response = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BookDto created = objectMapper.readValue(response, BookDto.class);

        mockMvc.perform(delete("/books/{id}", created.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBook_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/books/{id}", 9999L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Книга с ID 9999 не найдена")));
    }
}