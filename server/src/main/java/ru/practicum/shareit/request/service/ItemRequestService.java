package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(CreateItemRequestDto createItemRequestDto, Long userId);

    List<ResponseItemRequestDto> getRequests(Long userId);

    List<ResponseItemRequestDto> getAllRequests(Long userId);

    ResponseItemRequestDto getRequest(Long requestId, Long userId);
}
