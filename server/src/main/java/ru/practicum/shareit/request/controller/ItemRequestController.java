package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> createRequest(@RequestBody CreateItemRequestDto createItemRequestDto,
                                                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос пользователя c id: {} на добавление запроса: {}", userId, createItemRequestDto.toString());

        ItemRequestDto itemRequestDto = itemRequestService.createRequest(createItemRequestDto, userId);
        URI uri = URI.create("/requests/" + itemRequestDto.getId());

        return ResponseEntity.created(uri).body(itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<List<ResponseItemRequestDto>> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос пользователя c id: {} на получение своих запросов", userId);

        List<ResponseItemRequestDto> responseList = itemRequestService.getRequests(userId);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseItemRequestDto>> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос пользователя c id: {} на получение всех запросов", userId);

        List<ResponseItemRequestDto> responseList = itemRequestService.getAllRequests(userId);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ResponseItemRequestDto> getRequest(@PathVariable Long requestId,
                                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос пользователя c id: {} на получение запроса с id: {}", userId, requestId);
        ResponseItemRequestDto response = itemRequestService.getRequest(requestId, userId);
        return ResponseEntity.ok(response);
    }
}
