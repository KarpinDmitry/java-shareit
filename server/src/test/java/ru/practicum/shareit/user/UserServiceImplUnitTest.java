package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    private User user;
    private CreateUserDto createUserDto;
    private UpdateUserDto updateUserDto;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);

        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        createUserDto = new CreateUserDto();
        createUserDto.setName("John Doe");
        createUserDto.setEmail("john.doe@example.com");

        updateUserDto = new UpdateUserDto();
        updateUserDto.setName("Jane Doe");
        updateUserDto.setEmail("jane.doe@example.com");
    }

    @Test
    void createUser_shouldSaveUser() {
        // given
        when(userRepository.findByEmail(eq(createUserDto.getEmail()))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserDto result = userService.createUser(createUserDto);

        // then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_whenEmailAlreadyExists_shouldThrowIllegalArgumentException() {
        // given
        when(userRepository.findByEmail(eq(createUserDto.getEmail()))).thenReturn(Optional.of(user));

        // when + then
        assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(createUserDto));
    }

    @Test
    void getUserById_shouldReturnUser() {
        // given
        when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.of(user));

        // when
        UserDto result = userService.getUserById(user.getId());

        // then
        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getUserById_whenUserNotFound_shouldThrowNotFoundIdException() {
        // given
        when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundIdException.class, () ->
                userService.getUserById(user.getId()));
    }

    @Test
    void getUsers_shouldReturnAllUsers() {
        // given
        when(userRepository.findAll()).thenReturn(List.of(user));

        // when
        List<UserDto> result = userService.getUsers();

        // then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void updateUser_shouldUpdateUser() {
        // given
        when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(eq(updateUserDto.getEmail()))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserDto result = userService.updateUser(updateUserDto, user.getId());

        // then
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_whenUserNotFound_shouldThrowNotFoundIdException() {
        // given
        when(userRepository.findById(eq(user.getId()))).thenReturn(Optional.empty());

        // when + then
        assertThrows(NotFoundIdException.class, () ->
                userService.updateUser(updateUserDto, user.getId()));
    }

    @Test
    void deleteUser_shouldRemoveUser() {
        // when
        userService.deleteUser(user.getId());

        // then
        verify(userRepository).deleteById(eq(user.getId()));
    }
}