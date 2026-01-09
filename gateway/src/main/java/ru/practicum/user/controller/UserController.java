package ru.practicum.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.client.UserClient;
import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.UpdateUserDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Запрос на получение всех пользователей");
        return userClient.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@Positive @PathVariable Long id) {
        log.info("Запрос на получения пользователя с id: {}", id);
        return userClient.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        log.info("Запрос на добавления пользователя {}", createUserDto.toString());
        return userClient.createUser(createUserDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto,
                                             @Positive @PathVariable Long id) {
        log.info("Запрос на обновление пользователя {}", updateUserDto.toString());
        return userClient.updateUser(updateUserDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@Positive @PathVariable Long id) {
        log.info("Запрос на удаление пользователя c id: {}", id);
        return userClient.deleteUser(id);
    }
}
