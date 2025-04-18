package com.itgirl.library_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itgirl.library_project.Dto.AuthorDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthorDto authorDto;

    @BeforeEach
    void setup() {
        authorDto = AuthorDto.builder()
                .name("Test")
                .surname("Author")
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addNewAuthor_shouldReturnCreated() throws Exception {
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.surname").value("Author"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllAuthors_shouldReturnListAndView() throws Exception {
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("allAuthors"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAuthorById_shouldReturnAuthor() throws Exception {
        String response = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andReturn().getResponse().getContentAsString();

        AuthorDto created = objectMapper.readValue(response, AuthorDto.class);

        mockMvc.perform(get("/authors/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.surname").value("Author"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAuthorById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/authors/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("не существует")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateAuthor_shouldReturnUpdated() throws Exception {
        String response = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andReturn().getResponse().getContentAsString();

        AuthorDto created = objectMapper.readValue(response, AuthorDto.class);
        created.setName("Updated");
        created.setSurname("Name");

        mockMvc.perform(put("/authors/{id}", created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.surname").value("Name"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateAuthor_shouldReturnNotFound() throws Exception {
        authorDto.setName("Updated");
        mockMvc.perform(put("/authors/{id}", 123456)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteAuthor_shouldReturnNoContent() throws Exception {
        String response = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andReturn().getResponse().getContentAsString();

        AuthorDto created = objectMapper.readValue(response, AuthorDto.class);

        mockMvc.perform(delete("/authors/{id}", created.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteAuthor_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/authors/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAuthorsByQuery_shouldReturnAuthor() throws Exception {
        String name = "Name" + UUID.randomUUID();
        String surname = "Surname" + UUID.randomUUID();

        AuthorDto newAuthor = AuthorDto.builder().name(name).surname(surname).build();

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAuthor)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/authors/query")
                        .param("name", name)
                        .param("surname", surname))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].surname").value(surname));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAuthorsByQuery_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/authors/query")
                        .param("name", "NotFound" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("не найден")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAuthorsByCriteria_shouldReturnAuthor() throws Exception {
        String name = "Criteria" + UUID.randomUUID();
        AuthorDto newAuthor = AuthorDto.builder().name(name).surname("Sample").build();

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAuthor)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/authors/criteria")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").value(name));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAuthorsByCriteria_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/authors/criteria")
                        .param("name", "GhostName" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("не найден")));
    }
}