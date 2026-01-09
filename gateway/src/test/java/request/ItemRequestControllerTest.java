package request;

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
import ru.practicum.request.client.ItemRequestClient;
import ru.practicum.request.controller.ItemRequestController;
import ru.practicum.request.dto.CreateItemRequestDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ShareItGateway.class)
@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateItemRequestDto createItemRequestDto;

    @BeforeEach
    void setUp() {
        createItemRequestDto = new CreateItemRequestDto();
        createItemRequestDto.setDescription("Need a drill");
    }

    @Test
    void createRequest_shouldReturnCreatedRequest() throws Exception {
        when(itemRequestClient.createRequest(any(CreateItemRequestDto.class), eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getRequests_shouldReturnListOfRequests() throws Exception {
        when(itemRequestClient.getRequests(eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests_shouldReturnAllRequests() throws Exception {
        when(itemRequestClient.getAllRequests(eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getRequest_shouldReturnRequestById() throws Exception {
        when(itemRequestClient.getRequest(eq(1L), eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }
}