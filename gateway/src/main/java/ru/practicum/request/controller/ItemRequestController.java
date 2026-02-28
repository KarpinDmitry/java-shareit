package ru.practicum.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.client.ItemRequestClient;
import ru.practicum.request.dto.CreateItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@Valid @RequestBody CreateItemRequestDto createItemRequestDto,
                                                @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос пользователя c id: {} на добавление запроса: {}", userId, createItemRequestDto.toString());
        return itemRequestClient.createRequest(createItemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос пользователя c id: {} на получение своих запросов", userId);
        return itemRequestClient.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос пользователя c id: {} на получение всех запросов", userId);
        return itemRequestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable @Positive Long requestId,
                                             @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос пользователя c id: {} на получение запроса с id: {}", userId, requestId);
        return itemRequestClient.getRequest(requestId, userId);
    }
}
