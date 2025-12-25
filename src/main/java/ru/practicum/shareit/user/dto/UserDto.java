package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class UserDto {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String email;
}
