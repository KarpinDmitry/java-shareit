package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseItemRequestDto {
    @NonNull
    private Long id;
    @NonNull
    private String description;
    @NonNull
    private LocalDateTime created;

    private List<ItemDto> items;
}
