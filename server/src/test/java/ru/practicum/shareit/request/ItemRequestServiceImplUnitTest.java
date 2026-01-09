// src/test/java/ru/practicum/shareit/request/service/ItemRequestServiceImplUnitTest.java
package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemRequestServiceImpl itemRequestService;

    private User requestor;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(userRepository, itemRepository, itemRequestRepository);

        requestor = new User();
        requestor.setId(1L);
        requestor.setName("Requestor");
        requestor.setEmail("requestor@test.com");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Need a drill");
        itemRequest.setCreated(LocalDateTime.now()); // ← Исправлено: добавлено поле created
        itemRequest.setRequestor(requestor);
    }

    @Test
    void createRequest_shouldSaveRequest() {
        CreateItemRequestDto dto = new CreateItemRequestDto();
        dto.setDescription("Need a drill");

        when(userRepository.findById(eq(requestor.getId()))).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenAnswer(invocation -> {
            ItemRequest request = invocation.getArgument(0);
            request.setId(1L); // ← Имитируем генерацию ID
            return request;
        });

        ItemRequestDto result = itemRequestService.createRequest(dto, requestor.getId());

        assertNotNull(result);
        assertEquals("Need a drill", result.getDescription());
        assertEquals(1L, result.getId()); // ← Убедимся, что ID не null
        verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    void getRequests_shouldReturnRequestsByUser() {
        when(userRepository.findById(eq(requestor.getId()))).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.getRequestsByRequestor(eq(requestor.getId()))).thenReturn(List.of(itemRequest));
        when(itemRepository.getItemByRequest(eq(itemRequest.getId()))).thenReturn(List.of());

        List<ResponseItemRequestDto> result = itemRequestService.getRequests(requestor.getId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getRequest_shouldReturnRequestWithItems() {
        when(itemRequestRepository.findById(eq(itemRequest.getId()))).thenReturn(Optional.of(itemRequest));
        when(itemRepository.getItemByRequest(eq(itemRequest.getId()))).thenReturn(List.of());

        ResponseItemRequestDto result = itemRequestService.getRequest(itemRequest.getId(), requestor.getId());

        assertNotNull(result);
        assertEquals("Need a drill", result.getDescription());
    }
}