package ru.practicum.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateItemDto {
    @Positive
    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    private String description;
    private Boolean available;
}
