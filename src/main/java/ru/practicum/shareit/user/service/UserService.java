package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    UserDto getUserById(Integer userId);

    UserDto createUser(CreateUserDto createUserDto);

    UserDto updateUser(UpdateUserDto updateUserDto, Integer id);

    void deleteUser(Integer id);
}
