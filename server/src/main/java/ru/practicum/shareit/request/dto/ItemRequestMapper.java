package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequest toItemRequest(CreateItemRequestDto createItemRequestDto, User requestor, LocalDateTime now) {
        return new ItemRequest(
                null,
                createItemRequestDto.getDescription(),
                requestor,
                now
        );
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public static ResponseItemRequestDto toResponseItemCreateDto(ItemRequest itemRequest, List<ItemDto> items) {
        return new ResponseItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items
        );
    }
}
