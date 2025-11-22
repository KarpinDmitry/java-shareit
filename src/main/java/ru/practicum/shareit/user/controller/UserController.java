package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.net.URI;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("Запрос на получение всех пользователей");
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        log.info("Запрос на получения пользователя с id: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        log.info("Запрос на добавления пользователя {}", createUserDto.toString());
        UserDto createdUserDto = userService.createUser(createUserDto);
        URI uri = URI.create("/users/" + createdUserDto.getId());
        return ResponseEntity.created(uri).body(createdUserDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto,
                                              @PathVariable Integer id) {
        log.info("Запрос на обновление пользователя {}", updateUserDto.toString());
        return ResponseEntity.ok(userService.updateUser(updateUserDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        log.info("Запрос на удаление пользователя c id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
