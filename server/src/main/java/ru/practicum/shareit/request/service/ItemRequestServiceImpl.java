package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto createRequest(CreateItemRequestDto createItemRequestDto, Long userId) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundIdException("User с id " + userId + " не найден"));

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(createItemRequestDto, requestor, LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ResponseItemRequestDto> getRequests(Long userId) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundIdException("User с id " + userId + " не найден"));

        List<ItemRequest> requests = itemRequestRepository.getRequestsByRequestor(userId);

        List<ResponseItemRequestDto> result = new ArrayList<>();
        for (ItemRequest itemRequest : requests) {
            List<Item> items = itemRepository.getItemByRequest(itemRequest.getId());
            List<ItemDto> itemDtoList = items.stream().map(ItemMapper::toItemDto).toList();

            ResponseItemRequestDto responseItemRequestDto = ItemRequestMapper
                    .toResponseItemCreateDto(itemRequest, itemDtoList);

            result.add(responseItemRequestDto);
        }
        return result;
    }

    @Override
    public List<ResponseItemRequestDto> getAllRequests(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findAll();

        List<ResponseItemRequestDto> responseItemRequestDtoList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            List<Item> items = itemRepository.getItemByRequest(itemRequest.getId());
            List<ItemDto> itemDtoList = items.stream().map(ItemMapper::toItemDto).toList();

            ResponseItemRequestDto responseItemRequestDto = ItemRequestMapper
                    .toResponseItemCreateDto(itemRequest, itemDtoList);

            responseItemRequestDtoList.add(responseItemRequestDto);
        }
        return responseItemRequestDtoList;
    }

    @Override
    public ResponseItemRequestDto getRequest(Long requestId, Long userId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundIdException("Запрос с id: " + requestId + " не найден"));

        List<ItemDto> items = itemRepository.getItemByRequest(requestId)
                .stream().map(ItemMapper::toItemDto).toList();

        return ItemRequestMapper.toResponseItemCreateDto(itemRequest, items);
    }
}
