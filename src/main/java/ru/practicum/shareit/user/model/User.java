package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@AllArgsConstructor
public class User {
    private Integer id;
    @NonNull
    private String name;
    @Email(message = "Некорректный адрес электронной почты")
    @NonNull
    private String email;
}
