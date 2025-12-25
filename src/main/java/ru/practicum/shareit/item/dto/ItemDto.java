package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    private Boolean available;
    private Long requestId;
}
