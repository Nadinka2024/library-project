package com.itgirl.library_project.controller;

import com.itgirl.library_project.Dto.UserDto;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
@Tag(name = "Пользователи", description = "Управление списком пользователей")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Добавить нового пользователя")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addNewUser(@Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getAllErrors().forEach(objectError -> {
                if (objectError instanceof FieldError fieldError) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
            });
            log.info("Добавление нового пользователя: {}", userDto);
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(userService.addNewUser(userDto));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userService.getAllUsers().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@Valid @PathVariable Long id) {
        log.info("Получение пользователя по ID: {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@Valid @PathVariable Long id, @RequestBody UserDto userDto) {
        log.info("Изменения данных пользователя по ID: {}", id);
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Valid @PathVariable Long id) {
        log.info("Удаление пользователя по ID: {}", id);
        userService.deleteUser(id);
    }
}

