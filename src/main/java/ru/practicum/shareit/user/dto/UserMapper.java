package ru.practicum.shareit.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(CreateUserDto createUserDto) {
        return new User(
                null,
                createUserDto.getName(),
                createUserDto.getEmail()
        );
    }

    public static User toUser(UpdateUserDto updateUserDto, User user) {
        if (updateUserDto.getEmail() != null) {
            user.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getName() != null) {
            user.setName(updateUserDto.getName());
        }
        return user;
    }
}
