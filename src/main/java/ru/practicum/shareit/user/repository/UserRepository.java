package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getUsers();

    Optional<User> getUserById(Integer userId);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Integer id);

    Optional<User> findByEmail(String email);
}
