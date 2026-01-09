package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateItemRequestDto createItemRequestDto;
    private ItemRequestDto itemRequestDto;
    private ResponseItemRequestDto responseItemRequestDto;

    @BeforeEach
    void setUp() {
        createItemRequestDto = new CreateItemRequestDto();
        createItemRequestDto.setDescription("Need a drill");

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Need a drill");

        responseItemRequestDto = new ResponseItemRequestDto();
        responseItemRequestDto.setId(1L);
        responseItemRequestDto.setDescription("Need a drill");
    }

    @Test
    void createRequest_shouldReturnCreatedRequest() throws Exception {
        when(itemRequestService.createRequest(any(CreateItemRequestDto.class), eq(1L)))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/requests/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Need a drill"));
    }

    @Test
    void getRequests_shouldReturnListOfRequests() throws Exception {
        when(itemRequestService.getRequests(eq(1L)))
                .thenReturn(List.of(responseItemRequestDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Need a drill"));
    }

    @Test
    void getAllRequests_shouldReturnAllRequests() throws Exception {
        when(itemRequestService.getAllRequests(eq(1L)))
                .thenReturn(List.of(responseItemRequestDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getRequest_shouldReturnRequestById() throws Exception {
        when(itemRequestService.getRequest(eq(1L), eq(1L)))
                .thenReturn(responseItemRequestDto);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Need a drill"));
    }
}