package com.itgirl.library_project.servise;
import com.itgirl.library_project.Dto.UserDto;
import com.itgirl.library_project.Exception.ResourceNotFoundException;
import com.itgirl.library_project.entity.User;
import com.itgirl.library_project.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto addNewUser(UserDto userDto) {
        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        log.info("Создание нового пользователя с именем: {}", userDto.getName());

        User user = modelMapper.map(userDto, User.class);
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

        log.info("Пользователь успешно сохранен: {}", savedUser.getName());
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Transactional
    public List<UserDto> getAllUsers() {
        log.info("Получение списка всех пользователей");
        List<User> users = userRepository.findAll();
        log.info("Найдено {} пользователей", users.size());
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto getUserById(Long id) {
        log.info("Получение пользователя с ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        log.info("Обновление данных пользователя с ID: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        modelMapper.map(userDto, existingUser);

        User updatedUser = userRepository.save(existingUser);
        log.info("Данные пользователя с ID {} успешно обновлены", id);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Удаление пользователя с ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        userRepository.delete(user);
        log.info("Пользователь с ID {} успешно удален", id);
    }
}
