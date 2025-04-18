package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.UserDto;
import com.itgirl.library_project.entity.User;
import com.itgirl.library_project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

public
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        userDto = UserDto.builder()
                .name("Testuser")
                .email("testuser@gmail.com")
                .age(26)
                .password("12345")
                .role("ADMIN")
                .build();
    }

    @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    void addNewUser_Success(){
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(new User());
        when(modelMapper.map(any(), eq(UserDto.class))).thenReturn(userDto);

        UserDto result = userService.addNewUser(userDto);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("Testuser", result.getName());
        assertEquals("testuser@gmail.com", result.getEmail());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void addNewUser_unSuccess(){
        userDto.setPassword(null);
        assertThrows(IllegalArgumentException.class, () -> userService.addNewUser(userDto));
    }
}