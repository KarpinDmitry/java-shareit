package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public List<UserDto> getUsers() {
        return userRepository.getUsers()
                .stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundIdException("Id: " + id + " not found"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + createUserDto.getEmail());
        }

        User user = UserMapper.toUser(createUserDto);
        return UserMapper.toUserDto(userRepository.createUser(user));
    }

    @Override
    public UserDto updateUser(UpdateUserDto updateUserDto, Integer id) {
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundIdException("User with id: " + id + "not found"));

        if (userRepository.findByEmail(updateUserDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + updateUserDto.getEmail());
        }

        User updateUser = UserMapper.toUser(updateUserDto, user);
        return UserMapper.toUserDto(userRepository.updateUser(updateUser));
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteUser(id);
    }
}
