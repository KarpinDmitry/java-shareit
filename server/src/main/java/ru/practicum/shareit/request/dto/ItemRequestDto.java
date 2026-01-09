package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    @NonNull
    private Long id;
    @NonNull
    private String description;
    @NonNull
    private LocalDateTime created;
}
