package com.itgirl.library_project.controller;

import com.itgirl.library_project.Dto.UserDto;
import com.itgirl.library_project.Exception.ResourceNotFoundException;
import com.itgirl.library_project.servise.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    class UserControllerTest {

        @Mock
        private UserService userService;

        @Mock
        private BindingResult bindingResult;

        @InjectMocks
        private UserController userController;

        private UserDto getSampleUser() {
            return UserDto.builder()
                    .id(1L)
                    .name("Иван")
                    .email("ivan@gmail.com")
                    .build();
        }

        @Test
        @DisplayName("Добавление пользователя — успех")
        void addNewUser_success() {
            UserDto userDto = getSampleUser();
            when(userService.addNewUser(userDto)).thenReturn(userDto);
            when(bindingResult.hasErrors()).thenReturn(false);

            ResponseEntity<Object> response = userController.addNewUser(userDto, bindingResult);

            assertEquals(201, response.getStatusCodeValue());
            Map<String, Object> body = (Map<String, Object>) response.getBody();
            assertEquals("Пользователь успешно добавлен", body.get("message"));
            assertEquals(userDto, body.get("user"));
        }

        @Test
        @DisplayName("Добавление пользователя — ошибка валидации")
        void addNewUser_validationError() {
            UserDto invalidUser = UserDto.builder()
                    .name("")
                    .email("invalid")
                    .password("")
                    .age(10)
                    .build();

            when(bindingResult.hasErrors()).thenReturn(true);
            when(bindingResult.getFieldErrors()).thenReturn(List.of(
                    new FieldError("userDto", "name", "Логин не может быть пустым"),
                    new FieldError("userDto", "email", "Email не может быть пустым"),
                    new FieldError("userDto", "password", "Пароль не может быть пустым"),
                    new FieldError("userDto", "age", "Возраст не должен быть меньше 18")
            ));

            ResponseEntity<Object> response = userController.addNewUser(invalidUser, bindingResult);

            assertEquals(400, response.getStatusCodeValue());

            System.out.println("Response Body: " + response.getBody());

            Map<String, Object> body = (Map<String, Object>) response.getBody();
            assertEquals("Ошибка валидации", body.get("message"));

            Map<String, String> details = (Map<String, String>) body.get("details");

            assertTrue(details.containsKey("name"));
            assertEquals("Логин не может быть пустым", details.get("name"));

            assertTrue(details.containsKey("email"));
            assertEquals("Email не может быть пустым", details.get("email"));

            assertTrue(details.containsKey("password"));
            assertEquals("Пароль не может быть пустым", details.get("password"));

            assertTrue(details.containsKey("age"));
            assertEquals("Возраст не должен быть меньше 18", details.get("age"));
        }

        @Test
    @DisplayName("Получение всех пользователей — успех")
    void getAllUsers_success() {
        List<UserDto> users = List.of(getSampleUser());
        when(userService.getAllUsers()).thenReturn(users);

        List<UserDto> response = userController.getAllUsers();

        assertEquals(1, response.size());
        assertEquals("Иван", response.get(0).getName());
    }

    @Test
    @DisplayName("Получение пользователя по ID — успех")
    void getUserById_success() {
        UserDto user = getSampleUser();
        when(userService.getUserById(1L)).thenReturn(user);

        ResponseEntity<Object> response = userController.getUserById(1L);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Пользователь найден", body.get("message"));
        assertEquals(user, body.get("user"));
    }

    @Test
    @DisplayName("Получение пользователя по ID — не найден")
    void getUserById_notFound() {
        when(userService.getUserById(1L)).thenThrow(new ResourceNotFoundException("Пользователь не найден"));

        ResponseEntity<Object> response = userController.getUserById(1L);

        assertEquals(404, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertTrue(body.get("message").toString().contains("не найден"));
    }

    @Test
    @DisplayName("Обновление пользователя — успех")
    void updateUser_success() {
        UserDto user = getSampleUser();
        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(user);

        ResponseEntity<Object> response = userController.updateUser(1L, user);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Пользователь успешно обновлен", body.get("message"));
        assertEquals(user, body.get("user"));
    }

    @Test
    @DisplayName("Обновление пользователя — не найден")
    void updateUser_notFound() {
        UserDto user = getSampleUser();
        when(userService.updateUser(eq(1L), any(UserDto.class)))
                .thenThrow(new ResourceNotFoundException("Пользователь с ID 1 не найден"));

        ResponseEntity<Object> response = userController.updateUser(1L, user);

        assertEquals(404, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Ошибка обновления", body.get("message"));
        assertTrue(body.get("details").toString().contains("не найден"));
    }

    @Test
    @DisplayName("Удаление пользователя — успех")
    void deleteUser_success() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Map<String, String>> response = userController.deleteUser(1L);

        assertEquals(204, response.getStatusCodeValue());
        assertEquals("Пользователь успешно удален", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Удаление пользователя — не найден")
    void deleteUser_notFound() {
        doThrow(new ResourceNotFoundException("Пользователь с ID 1 не найден"))
                .when(userService).deleteUser(1L);

        ResponseEntity<Map<String, String>> response = userController.deleteUser(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().get("message").contains("не найден"));
    }
}