package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> userMap = new HashMap<>();

    private int id = 0;

    @Override
    public List<User> getUsers() {
        return userMap.values().stream().toList();
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public User createUser(User user) {
        user.setId(getId());
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        Integer id = user.getId();
        if (id == null || !userMap.containsKey(id)) {
            throw new NotFoundIdException("User id: " + id + " not found");
        }

        userMap.put(id, user);
        return user;
    }

    @Override
    public void deleteUser(Integer id) {
        if (id == null || !userMap.containsKey(id)) {
            throw new NotFoundIdException("Id: " + id + " not found");
        }
        userMap.remove(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMap.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    private int getId() {
        return ++id;
    }
}
