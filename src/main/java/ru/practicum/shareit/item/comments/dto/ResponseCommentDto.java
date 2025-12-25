package ru.practicum.shareit.item.comments.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCommentDto {
    @NonNull
    private Long id;
    @NonNull
    private String text;
    @NonNull
    private String authorName;
    @NonNull
    private LocalDate created;
}
