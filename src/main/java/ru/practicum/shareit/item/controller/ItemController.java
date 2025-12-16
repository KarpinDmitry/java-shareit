package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @Valid @RequestBody CreateItemDto createItemDto) {
        log.info("Запрос на добавление вещи {} пользователем: {}", createItemDto.toString(), userId);
        ItemDto itemDto = itemService.createItem(createItemDto, userId);
        URI uri = URI.create("/items" + itemDto.getId());
        return ResponseEntity.created(uri).body(itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@PathVariable Long itemId,
                                          @Valid @RequestBody UpdateItemDto updateItemDto,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на обновление вещи  {} пользователем: {}", updateItemDto.toString(), userId);
        ItemDto itemDto = itemService.updateItem(updateItemDto, itemId, userId);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long itemId,
                                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на просмотр вещи с id: {}", itemId);
        ItemDto itemDto = itemService.getItemById(itemId, userId);
        return ResponseEntity.ok(itemDto);
    }

    //Запрос пользователя на просмотр его вещей
    @GetMapping
    public ResponseEntity<List<ItemDto>> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос пользователя c id {} на просмотр вещей", userId);
        List<ItemDto> itemDtoList = itemService.getUserItems(userId);
        return ResponseEntity.ok(itemDtoList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam String text) {
        List<ItemDto> itemDtoList = itemService.searchItem(userId, text);
        return ResponseEntity.ok(itemDtoList);
    }

}
