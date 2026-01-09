package ru.practicum.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateItemRequestDto {
    @NotBlank
    private String description;
}
