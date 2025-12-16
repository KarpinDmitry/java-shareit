package ru.practicum.shareit.booking.controller;


import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ResponseBookingDto> createBooking(@Valid @RequestBody CreateBookingDto createBookingDto,
                                                            @RequestHeader("X-Sharer-User-Id") Long bookerId){
        log.info("Запрос на создание бронирования вещи с id: {}, bookerId: {}", createBookingDto.getItemId(), bookerId);
        ResponseBookingDto bookingDto = bookingService.createBooking(createBookingDto, bookerId);
        URI uri = URI.create("/bookings/" + bookingDto.getId());
        return ResponseEntity.created(uri).body(bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<ResponseBookingDto> bookingConfirmation(@PathVariable Long bookingId,
                                                          @RequestParam Boolean approved,
                                                          @RequestHeader("X-Sharer-User-Id") Long ownerId){
        log.info("Запрос на подтверждение бронирования с id {} owner: {}, статус {}", bookingId, ownerId, approved);
        if (approved == null) {
            throw new ValidationException("Параметр approved обязателен");
        }
        ResponseBookingDto bookingDto = bookingService.bookingConfirmation(bookingId, approved, ownerId);
        return ResponseEntity.ok(bookingDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ResponseBookingDto> getBooking(@PathVariable Long bookingId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId){
        log.info("Запрос на получение информации о бронировании с id {} user {}", bookingId, userId);
        ResponseBookingDto bookingDto = bookingService.getBooking(bookingId, userId);
        return ResponseEntity.ok(bookingDto);
    }

    //Запрос на получение бронирований пользователя
    @GetMapping
    public ResponseEntity<List<ResponseBookingDto>> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Запрос на получение бронирований пользователя: {}", userId);
        List<ResponseBookingDto> bookings = bookingService.getBookingsByUserAndState(userId, state);
        return ResponseEntity.ok(bookings);
    }

    //Запрос на получение списка бронирований всех вещей текущего пользователя
    @GetMapping("/owner")
    public ResponseEntity<List<ResponseBookingDto>> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(defaultValue = "ALL") BookingState state){
        log.info("Запрос на получение списка бронирований всех вещей текущего пользователя: {}", userId);
        List<ResponseBookingDto> bookings = bookingService.getOwnerBookings(userId, state);
        return ResponseEntity.ok(bookings);
    }
}
