package ru.practicum.booking.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.client.BookingClient;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.booking.dto.CreateBookingDto;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@Valid @RequestBody CreateBookingDto createBookingDto,
                                                @RequestHeader("X-Sharer-User-Id") @Positive Long bookerId) {
        log.info("Запрос на создание бронирования вещи с id: {}, bookerId: {}", createBookingDto.getItemId(), bookerId);
        return bookingClient.createBooking(createBookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> bookingConfirmation(@PathVariable @Positive Long bookingId,
                                                      @RequestParam(required = true) Boolean approved,
                                                      @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Запрос на подтверждение бронирования с id {} owner: {}, статус {}", bookingId, ownerId, approved);
        return bookingClient.bookingConfirmation(bookingId, approved, ownerId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable @Positive Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.info("Запрос на получение информации о бронировании с id {} user {}", bookingId, userId);
        return bookingClient.getBooking(bookingId, userId);
    }

    //Запрос на получение бронирований пользователя
    @GetMapping
    public ResponseEntity<Object> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Запрос на получение бронирований пользователя: {}", userId);
        return bookingClient.getBookingsByUserAndState(userId, state);
    }

    //Запрос на получение списка бронирований всех вещей текущего пользователя
    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Запрос на получение списка бронирований всех вещей текущего пользователя: {}", userId);
        return bookingClient.getOwnerBookings(userId, state);
    }
}
