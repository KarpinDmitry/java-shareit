package ru.practicum.shareit.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserDto {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String email;
}
