package ru.practicum.shareit.booking.service;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    ResponseBookingDto createBooking(CreateBookingDto createBookingDto, Long bookerId);

    ResponseBookingDto bookingConfirmation(Long bookingId, Boolean approved, Long ownerId);

    ResponseBookingDto getBooking(Long bookingId, Long userId);

    List<ResponseBookingDto> getBookingsByUserAndState(Long userId, BookingState state);

    List<ResponseBookingDto> getOwnerBookings(Long userId, BookingState state);
}
