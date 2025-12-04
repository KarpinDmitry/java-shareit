package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IllegalOwnerException;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto createItem(CreateItemDto createItemDto, Integer userId) {
        userService.getUserById(userId); //проверка существования юзера

        Item item = ItemMapper.toItem(createItemDto, userId);
        return ItemMapper.toItemDto(itemRepository.createItem(item));
    }

    @Override
    public ItemDto updateItem(UpdateItemDto updateItemDto, Integer id, Integer userId) {
        Item item = itemRepository.getItemById(id)
                .orElseThrow(() -> new NotFoundIdException("Item с id: " + id + "не найден"));

        Item updateItem = ItemMapper.toItem(updateItemDto, item);
        if (!item.getOwner().equals(userId)) {
            throw new IllegalOwnerException("Владелец не совпадает c пользователем: " + item.getOwner() + "!=" + userId);
        }
        return ItemMapper.toItemDto(itemRepository.updateItem(updateItem));

    }

    @Override
    public ItemDto getItemById(Integer id, Integer userId) {
        Item item = itemRepository.getItemById(id)
                .orElseThrow(() -> new NotFoundIdException("Item с id: " + id + "не найден"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItems(Integer userId) {
        return itemRepository.getItems(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> searchItem(Integer userId, String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return itemRepository.searchItem(text).stream().map(ItemMapper::toItemDto).toList();
    }
}
