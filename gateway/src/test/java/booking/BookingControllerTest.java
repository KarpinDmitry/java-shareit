package booking;

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
import ru.practicum.booking.client.BookingClient;
import ru.practicum.booking.controller.BookingController;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.booking.dto.CreateBookingDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ShareItGateway.class)
@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateBookingDto createBookingDto;

    @BeforeEach
    void setUp() {
        createBookingDto = new CreateBookingDto();
        createBookingDto.setItemId(1L);
        createBookingDto.setStart(LocalDateTime.now().plusHours(1));
        createBookingDto.setEnd(LocalDateTime.now().plusHours(2));
    }

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        when(bookingClient.createBooking(any(CreateBookingDto.class), eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isOk());
    }

    @Test
    void bookingConfirmation_shouldReturnApprovedBooking() throws Exception {
        when(bookingClient.bookingConfirmation(eq(1L), eq(true), eq(2L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void bookingConfirmation_whenApprovedIsRequired_shouldFailIfMissing() throws Exception {
        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getBooking_shouldReturnBooking() throws Exception {
        when(bookingClient.getBooking(eq(1L), eq(1L)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_shouldReturnListOfBookings() throws Exception {
        when(bookingClient.getBookingsByUserAndState(eq(1L), any(BookingState.class)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getOwnerBookings_shouldReturnListOfBookings() throws Exception {
        when(bookingClient.getOwnerBookings(eq(1L), any(BookingState.class)))
                .thenReturn(org.springframework.http.ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }
}