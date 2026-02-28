package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comments.dto.CreateCommentDto;
import ru.practicum.shareit.item.comments.dto.ResponseCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(CreateItemDto createItemDto, Long userId);

    ItemDto updateItem(UpdateItemDto updateItemDto, Long id, Long userId);

    ResponseItemDto getItemById(Long id, Long userId);

    List<ResponseItemDto> getUserItems(Long userId);

    List<ItemDto> searchItem(Long userId, String text);

    ResponseCommentDto createComment(CreateCommentDto createCommentDto, Long itemId, Long userId);

}

