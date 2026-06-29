package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    private CreateUserDto createUserDto;

    @BeforeEach
    void setUp() {
        createUserDto = new CreateUserDto();
        createUserDto.setName("John Doe");
        createUserDto.setEmail("john.doe@example.com");
    }

    @Test
    void createUser_shouldSaveUser() {
        UserDto result = userService.createUser(createUserDto);

        assertNotNull(result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void getUserById_shouldReturnUser() {
        UserDto savedUser = userService.createUser(createUserDto);

        UserDto result = userService.getUserById(savedUser.getId());

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    void getUsers_shouldReturnAllUsers() {
        userService.createUser(createUserDto);

        List<UserDto> users = userService.getUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
    }

    @Test
    void updateUser_shouldUpdateUser() {
        UserDto savedUser = userService.createUser(createUserDto);

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setName("Jane Doe");
        updateUserDto.setEmail("jane.doe@example.com");

        UserDto updatedUser = userService.updateUser(updateUserDto, savedUser.getId());

        assertEquals("Jane Doe", updatedUser.getName());
        assertEquals("jane.doe@example.com", updatedUser.getEmail());
    }

    @Test
    void deleteUser_shouldRemoveUser() {
        UserDto savedUser = userService.createUser(createUserDto);

        userService.deleteUser(savedUser.getId());

        assertThrows(RuntimeException.class, () -> userService.getUserById(savedUser.getId()));
    }
}