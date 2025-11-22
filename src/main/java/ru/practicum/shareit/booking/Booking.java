package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    @NonNull
    private Integer id;
    @NonNull
    private LocalDateTime start;
    @NonNull
    private LocalDateTime end;
    @NonNull
    private Integer item;
    @NonNull
    private Integer booker;
    @NonNull
    private BookingStatus status;
}
