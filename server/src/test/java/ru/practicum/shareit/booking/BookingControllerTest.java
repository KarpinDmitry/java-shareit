package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateBookingDto createBookingDto;
    private ResponseBookingDto responseBookingDto;

    @BeforeEach
    void setUp() {
        createBookingDto = new CreateBookingDto();
        createBookingDto.setItemId(1L);
        createBookingDto.setStart(LocalDateTime.now().plusHours(1));
        createBookingDto.setEnd(LocalDateTime.now().plusHours(2));

        responseBookingDto = new ResponseBookingDto();
        responseBookingDto.setId(1L);
        responseBookingDto.setStatus(BookingStatus.WAITING);
    }

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        when(bookingService.createBooking(any(CreateBookingDto.class), eq(1L)))
                .thenReturn(responseBookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/bookings/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void bookingConfirmation_shouldReturnApprovedBooking() throws Exception {
        when(bookingService.bookingConfirmation(eq(1L), eq(true), eq(2L)))
                .thenReturn(responseBookingDto);

        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void bookingConfirmation_whenApprovedIsNull_shouldThrowValidationException() throws Exception {
        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getBooking_shouldReturnBooking() throws Exception {
        when(bookingService.getBooking(eq(1L), eq(1L)))
                .thenReturn(responseBookingDto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllBookings_shouldReturnListOfBookings() throws Exception {
        when(bookingService.getBookingsByUserAndState(eq(1L), eq(BookingState.ALL)))
                .thenReturn(List.of(responseBookingDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getOwnerBookings_shouldReturnListOfBookings() throws Exception {
        when(bookingService.getOwnerBookings(eq(1L), eq(BookingState.ALL)))
                .thenReturn(List.of(responseBookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}