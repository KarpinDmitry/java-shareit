package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@AllArgsConstructor
public class ItemRequest {
    @NonNull
    private Integer id;
    @NonNull
    private String description;
    @NonNull
    private Integer requestor;
    @NonNull
    private LocalDateTime created;

}
