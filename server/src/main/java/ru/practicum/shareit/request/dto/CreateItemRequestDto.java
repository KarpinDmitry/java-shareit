package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateItemRequestDto {
    private String description;
}
