package com.itgirl.library_project.controller;

import com.itgirl.library_project.Dto.UserDto;
import com.itgirl.library_project.Exception.ResourceNotFoundException;
import com.itgirl.library_project.servise.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
@Tag(name = "Пользователи", description = "Управление списком пользователей")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Добавить нового пользователя", description = "Добавляет нового пользователя в систему.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addNewUser(@Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            });

            log.error("Ошибка валидации при добавлении нового пользователя: {}", errors);
            return ResponseEntity.badRequest().body(Map.of("message", "Ошибка валидации", "details", errors));
        }

        try {
            UserDto createdUser = userService.addNewUser(userDto);
            log.info("Пользователь успешно добавлен: {}", createdUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Пользователь успешно добавлен", "user", createdUser));
        } catch (Exception e) {
            log.error("Ошибка при добавлении пользователя: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Ошибка при добавлении пользователя"));
        }
    }

    @Operation(summary = "Получить список всех пользователей", description = "Возвращает список всех пользователей в системе.")
    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Запрос на получение списка всех пользователей");
        List<UserDto> users = userService.getAllUsers();
        if (users.isEmpty()) {
            log.info("Нет пользователей для отображения.");
        }
        return users;
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает данные пользователя по его ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@Valid @PathVariable Long id) {
        try {
            log.info("Получение пользователя по ID: {}", id);
            UserDto userDto = userService.getUserById(id);
            return ResponseEntity.ok(Map.of("message", "Пользователь найден", "user", userDto));
        } catch (ResourceNotFoundException e) {
            log.error("Пользователь с ID {} не найден", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Пользователь c id " + id + " не найден"));
        }
    }

    @Operation(summary = "Обновить данные пользователя", description = "Обновляет данные пользователя по его ID.")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@Valid @PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            log.info("Обновление пользователя с ID: {}", id);
            UserDto updatedUser = userService.updateUser(id, userDto);
            return ResponseEntity.ok(Map.of("message", "Пользователь успешно обновлен", "user", updatedUser));
        } catch (ResourceNotFoundException e) {
            log.error("Ошибка обновления: пользователь с ID {} не найден", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Ошибка обновления", "details", "Пользователь с ID " + id + " не найден"));
        }
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по его ID.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, String>> deleteUser(@Valid @PathVariable Long id) {
        try {
            log.info("Удаление пользователя с ID: {}", id);
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Map.of("message", "Пользователь успешно удален"));
        } catch (ResourceNotFoundException e) {
            log.error("Ошибка удаления: пользователь с ID {} не найден", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Пользователь с ID " + id + " не найден"));
        }
    }
}
