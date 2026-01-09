package ru.practicum.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.client.ItemClient;
import ru.practicum.item.comment.dto.CreateCommentDto;
import ru.practicum.item.dto.CreateItemDto;
import ru.practicum.item.dto.UpdateItemDto;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                              @Valid @RequestBody CreateItemDto createItemDto) {
        log.info("Запрос на добавление вещи {} пользователем: {}", createItemDto.toString(), userId);
        return itemClient.createItem(createItemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Positive @PathVariable Long itemId,
                                          @Valid @RequestBody UpdateItemDto updateItemDto,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на обновление вещи  {} пользователем: {}", updateItemDto.toString(), userId);
        return itemClient.updateItem(updateItemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@Positive @PathVariable Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос на просмотр вещи с id: {}", itemId);
        return itemClient.getItemById(itemId, userId);
    }

    //Запрос пользователя на просмотр его вещей
    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос пользователя c id {} на просмотр вещей", userId);
        return itemClient.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                             @RequestParam @NotBlank String text) {
        return itemClient.searchItem(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CreateCommentDto createCommentDto,
                                                @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                @PathVariable @Positive Long itemId) {
        log.info("Запрос на добавление отзыва для вещи: {}", itemId);
        return itemClient.createComment(createCommentDto, itemId, userId);
    }
}
