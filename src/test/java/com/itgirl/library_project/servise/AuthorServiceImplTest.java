package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.AuthorDto;
import com.itgirl.library_project.Exception.ResourceNotFoundException;
import com.itgirl.library_project.entity.Author;
import com.itgirl.library_project.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private AuthorDto authorDto;
    private Author author;

    @BeforeEach
    void setUp() {
        authorDto = new AuthorDto(1L, "Лев", "Толстой");
        author = new Author(1L, "Лев", "Толстой", new ArrayList<>());
    }

    @Test
    void addNewAuthor_Success() {
        AuthorDto authorDto = new AuthorDto(null, "Лев", "Толстой");
        Author savedAuthor = new Author(1L, "Лев", "Толстой", new ArrayList<>());
        AuthorDto savedAuthorDto = new AuthorDto(1L, "Лев", "Толстой");

        when(modelMapper.map(authorDto, Author.class)).thenReturn(savedAuthor);
        when(authorRepository.save(savedAuthor)).thenReturn(savedAuthor);
        when(modelMapper.map(savedAuthor, AuthorDto.class)).thenReturn(savedAuthorDto);

        AuthorDto result = authorService.addNewAuthor(authorDto);

        assertNotNull(result);
        assertEquals(savedAuthorDto.getId(), result.getId());
        verify(authorRepository, times(1)).save(savedAuthor);
    }

    @Test
    void addNewAuthor_Failure() {
        AuthorDto authorDto = new AuthorDto(null, "Лев", "Толстой");

        when(modelMapper.map(authorDto, Author.class)).thenThrow(new RuntimeException("Ошибка при добавлении"));

        assertThrows(RuntimeException.class, () -> authorService.addNewAuthor(authorDto));
    }

    @Test
    void addNewAuthor_Error() {
        when(modelMapper.map(authorDto, Author.class)).thenReturn(author);
        when(authorRepository.save(author)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> authorService.addNewAuthor(authorDto));
    }

    @Test
    void getAllAuthors_Success() {
        Author author1 = new Author(1L, "Лев", "Толстой", new ArrayList<>());
        Author author2 = new Author(2L, "Антон", "Чехов", new ArrayList<>());
        List<Author> authors = Arrays.asList(author1, author2);
        AuthorDto authorDto1 = new AuthorDto(1L, "Лев", "Толстой");
        AuthorDto authorDto2 = new AuthorDto(2L, "Антон", "Чехов");

        when(authorRepository.findAll()).thenReturn(authors);
        when(modelMapper.map(author1, AuthorDto.class)).thenReturn(authorDto1);
        when(modelMapper.map(author2, AuthorDto.class)).thenReturn(authorDto2);

        List<AuthorDto> result = authorService.getAllAuthors(null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Лев", result.get(0).getName());
        assertEquals("Антон", result.get(1).getName());
    }

    @Test
    void getAuthorById_Success() {
        Author author = new Author(1L, "Лев", "Толстой", new ArrayList<>());
        AuthorDto authorDto = new AuthorDto(1L, "Лев", "Толстой");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(modelMapper.map(author, AuthorDto.class)).thenReturn(authorDto);

        AuthorDto result = authorService.getAuthorById(1L);

        assertNotNull(result);
        assertEquals(authorDto.getId(), result.getId());
    }

    @Test
    void getAuthorById_Failure_NotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthorById(1L));
    }

    @Test
    void getAuthorById_NotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthorById(1L));
    }

    @Test
    void updateAuthor_Success() {
        Author existingAuthor = new Author(1L, "СтароеИмя", "СтараяФамилия", new ArrayList<>());
        AuthorDto updatedAuthorDto = new AuthorDto(1L, "Лев", "Толстой");
        Author updatedAuthor = new Author(1L, "Лев", "Толстой", new ArrayList<>());

        when(authorRepository.findById(1L)).thenReturn(Optional.of(existingAuthor));

        doAnswer(invocation -> {
            AuthorDto source = invocation.getArgument(0);
            Author destination = invocation.getArgument(1);
            destination.setName(source.getName());
            destination.setSurname(source.getSurname());
            return null;
        }).when(modelMapper).map(any(AuthorDto.class), any(Author.class));

        when(authorRepository.save(existingAuthor)).thenReturn(existingAuthor);
        when(modelMapper.map(existingAuthor, AuthorDto.class)).thenReturn(updatedAuthorDto);

        AuthorDto result = authorService.updateAuthor(1L, updatedAuthorDto);

        assertNotNull(result);
        assertEquals("Лев", result.getName());
        assertEquals("Толстой", result.getSurname());

        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void updateAuthor_Failure_NotFound() {
        AuthorDto updatedAuthorDto = new AuthorDto(1L, "Лев", "Толстой");

        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.updateAuthor(1L, updatedAuthorDto));
    }

    @Test
    void deleteAuthor_Success() {
        Author author = new Author(1L, "Лев", "Толстой", new ArrayList<>());

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).delete(author);
    }

    @Test
    void deleteAuthor_Failure_NotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.deleteAuthor(1L));
    }

    @Test
    void getAuthorsByNameOrSurname_Success() {
        Author author1 = new Author(1L, "Лев", "Толстой", new ArrayList<>());
        AuthorDto authorDto1 = new AuthorDto(1L, "Лев", "Толстой");

        when(authorRepository.findByNameOrSurname("Лев", "Толстой")).thenReturn(Arrays.asList(author1));
        when(modelMapper.map(author1, AuthorDto.class)).thenReturn(authorDto1);

        List<AuthorDto> result = authorService.getAuthorsByNameOrSurname("Лев", "Толстой");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Лев", result.get(0).getName());
    }

    @Test
    void getAuthorsByNameWithCriteria_Success() {
        Author author1 = new Author(1L, "Лев", "Толстой", new ArrayList<>());
        AuthorDto authorDto1 = new AuthorDto(1L, "Лев", "Толстой");

        when(authorRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(author1));
        when(modelMapper.map(author1, AuthorDto.class)).thenReturn(authorDto1);

        List<AuthorDto> result = authorService.getAuthorsByNameWithCriteria("Лев", "Толстой");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Лев", result.get(0).getName());
    }
}