package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class ItemRequestServiceImplIntegrationTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @PersistenceContext
    private EntityManager em;

    private User requestor;

    @BeforeEach
    void setUp() {
        requestor = new User();
        requestor.setName("Requestor");
        requestor.setEmail("requestor@test.com");
        userRepository.save(requestor);

        em.flush();
    }

    @Test
    void createRequest_shouldSaveRequest() {
        CreateItemRequestDto dto = new CreateItemRequestDto();
        dto.setDescription("Need a drill");

        ItemRequestDto result = itemRequestService.createRequest(dto, requestor.getId());

        assertNotNull(result.getId());
        assertEquals("Need a drill", result.getDescription());
    }

    @Test
    void getRequests_shouldReturnRequestsByUser() {
        CreateItemRequestDto dto = new CreateItemRequestDto();
        dto.setDescription("Need a drill");
        itemRequestService.createRequest(dto, requestor.getId());

        List<ResponseItemRequestDto> result = itemRequestService.getRequests(requestor.getId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Need a drill", result.get(0).getDescription());
    }

    @Test
    void getAllRequests_shouldReturnAllRequests() {
        CreateItemRequestDto dto = new CreateItemRequestDto();
        dto.setDescription("Need a drill");
        itemRequestService.createRequest(dto, requestor.getId());

        List<ResponseItemRequestDto> result = itemRequestService.getAllRequests(requestor.getId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getRequest_shouldReturnRequestWithItems() {
        CreateItemRequestDto dto = new CreateItemRequestDto();
        dto.setDescription("Need a drill");
        ItemRequestDto requestDto = itemRequestService.createRequest(dto, requestor.getId());

        ResponseItemRequestDto result = itemRequestService.getRequest(requestDto.getId(), requestor.getId());

        assertNotNull(result);
        assertEquals("Need a drill", result.getDescription());
    }
}