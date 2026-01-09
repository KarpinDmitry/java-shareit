package item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ShareItGateway;
import ru.practicum.item.client.ItemClient;
import ru.practicum.item.comment.dto.CreateCommentDto;
import ru.practicum.item.controller.ItemController;
import ru.practicum.item.dto.CreateItemDto;
import ru.practicum.item.dto.UpdateItemDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ShareItGateway.class)
@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateItemDto createItemDto;
    private UpdateItemDto updateItemDto;
    private CreateCommentDto createCommentDto;

    @BeforeEach
    void setUp() {
        createItemDto = new CreateItemDto();
        createItemDto.setName("Drill");
        createItemDto.setDescription("Power drill");
        createItemDto.setAvailable(true);

        updateItemDto = new UpdateItemDto();
        updateItemDto.setName("Updated Drill");

        createCommentDto = new CreateCommentDto();
        createCommentDto.setText("Great item!");
    }

    @Test
    void createItem_shouldReturnCreatedItem() throws Exception {
        when(itemClient.createItem(any(CreateItemDto.class), eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {
        when(itemClient.updateItem(any(UpdateItemDto.class), eq(1L), eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getItemById_shouldReturnItem() throws Exception {
        when(itemClient.getItemById(eq(1L), eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getUserItems_shouldReturnListOfItems() throws Exception {
        when(itemClient.getUserItems(eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void searchItem_shouldReturnMatchingItems() throws Exception {
        when(itemClient.searchItem(eq(1L), eq("drill")))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "drill"))
                .andExpect(status().isOk());
    }

    @Test
    void createComment_shouldReturnCreatedComment() throws Exception {
        when(itemClient.createComment(any(CreateCommentDto.class), eq(1L), eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isOk());
    }
}