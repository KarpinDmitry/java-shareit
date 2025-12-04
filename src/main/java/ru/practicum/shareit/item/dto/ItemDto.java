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
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    private boolean available;
    private Integer requestId;
}
