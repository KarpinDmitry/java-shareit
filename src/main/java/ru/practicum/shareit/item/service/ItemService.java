package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(CreateItemDto createItemDto, Integer userId);

    ItemDto updateItem(UpdateItemDto updateItemDto, Integer id, Integer userId);

    ItemDto getItemById(Integer id, Integer userId);

    List<ItemDto> getItems(Integer userId);

    List<ItemDto> searchItem(Integer userId, String text);
}
