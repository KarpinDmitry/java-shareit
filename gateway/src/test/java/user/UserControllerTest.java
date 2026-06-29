package user;

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
import ru.practicum.user.client.UserClient;
import ru.practicum.user.controller.UserController;
import ru.practicum.user.dto.CreateUserDto;
import ru.practicum.user.dto.UpdateUserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ShareItGateway.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserClient userClient;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateUserDto createUserDto;
    private UpdateUserDto updateUserDto;

    @BeforeEach
    void setUp() {
        createUserDto = new CreateUserDto();
        createUserDto.setName("John Doe");
        createUserDto.setEmail("john.doe@example.com");

        updateUserDto = new UpdateUserDto();
        updateUserDto.setName("Jane Doe");
        updateUserDto.setEmail("jane.doe@example.com");
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        when(userClient.createUser(any(CreateUserDto.class)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        when(userClient.getUserById(eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getUsers_shouldReturnListOfUsers() throws Exception {
        when(userClient.getUsers())
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        when(userClient.updateUser(any(UpdateUserDto.class), eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        when(userClient.deleteUser(eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.noContent().build());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}