package com.itgirl.library_project.servise;

import com.itgirl.library_project.Dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addNewUser(UserDto userDto);

     List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);
}
