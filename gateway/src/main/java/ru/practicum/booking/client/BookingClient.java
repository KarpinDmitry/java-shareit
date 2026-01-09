package ru.practicum.booking.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.booking.dto.CreateBookingDto;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(CreateBookingDto createBookingDto, Long userId) {
        return post("", userId, createBookingDto);
    }

    public ResponseEntity<Object> bookingConfirmation(Long bookingId, Boolean approved, Long userId) {
        Map<String, Object> parameter = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", userId, parameter, null);
    }

    public ResponseEntity<Object> getBooking(Long bookingId, Long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingsByUserAndState(Long userId, BookingState bookingState) {
        Map<String, Object> parameter = Map.of("state", bookingState);
        return get("?state={state}", userId, parameter);
    }

    public ResponseEntity<Object> getOwnerBookings(Long userId, BookingState bookingState) {
        Map<String, Object> parameter = Map.of("state", bookingState);
        return get("/owner?state={state}", userId, parameter);
    }
}