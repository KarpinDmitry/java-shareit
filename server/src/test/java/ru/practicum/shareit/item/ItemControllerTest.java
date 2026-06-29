package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comments.dto.CreateCommentDto;
import ru.practicum.shareit.item.comments.dto.ResponseCommentDto;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateItemDto createItemDto;
    private UpdateItemDto updateItemDto;
    private ItemDto itemDto;
    private ResponseItemDto responseItemDto;
    private CreateCommentDto createCommentDto;
    private ResponseCommentDto responseCommentDto;

    @BeforeEach
    void setUp() {
        createItemDto = new CreateItemDto();
        createItemDto.setName("Drill");
        createItemDto.setDescription("Power drill");
        createItemDto.setAvailable(true);

        updateItemDto = new UpdateItemDto();
        updateItemDto.setName("Updated Drill");

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Drill");
        itemDto.setDescription("Power drill");
        itemDto.setAvailable(true);

        responseItemDto = new ResponseItemDto();
        responseItemDto.setId(1L);
        responseItemDto.setName("Drill");

        createCommentDto = new CreateCommentDto();
        createCommentDto.setText("Great item!");

        responseCommentDto = new ResponseCommentDto();
        responseCommentDto.setId(1L);
        responseCommentDto.setText("Great item!");
    }

    @Test
    void createItem_shouldReturnCreatedItem() throws Exception {
        when(itemService.createItem(any(CreateItemDto.class), eq(1L)))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/items/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Drill"));
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {
        when(itemService.updateItem(any(UpdateItemDto.class), eq(1L), eq(1L)))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getItemById_shouldReturnItem() throws Exception {
        when(itemService.getItemById(eq(1L), eq(1L)))
                .thenReturn(responseItemDto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Drill"));
    }

    @Test
    void getUserItems_shouldReturnListOfItems() throws Exception {
        when(itemService.getUserItems(eq(1L)))
                .thenReturn(List.of(responseItemDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void searchItem_shouldReturnMatchingItems() throws Exception {
        when(itemService.searchItem(eq(1L), eq("drill")))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "drill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Drill"));
    }

    @Test
    void createComment_shouldReturnCreatedComment() throws Exception {
        when(itemService.createComment(any(CreateCommentDto.class), eq(1L), eq(1L)))
                .thenReturn(responseCommentDto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Great item!"));
    }
}