package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IllegalOwnerException;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.item.dto.*;
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
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto createItem(CreateItemDto createItemDto, Long userId) {
        userService.getUserById(userId); //проверка существования юзера

        Item item = ItemMapper.toItem(createItemDto, userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(UpdateItemDto updateItemDto, Long id, Long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Item с id: " + id + "не найден"));

        Item updateItem = ItemMapper.toItem(updateItemDto, item);
        if (!item.getOwnerId().equals(userId)) {
            throw new IllegalOwnerException("Владелец не совпадает c пользователем: " + item.getOwnerId() + "!=" + userId);
        }
        return ItemMapper.toItemDto(itemRepository.save(updateItem));

    }

    @Override
    public ResponseItemDto getItemById(Long id, Long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Item с id: " + id + "не найден"));

        Booking booking = bookingRepository.findById()
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ResponseItemDto> getUserItems(Long userId) {
        return itemRepository.getUserItems(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return itemRepository.search(text).stream().map(ItemMapper::toItemDto).toList();
    }
}
