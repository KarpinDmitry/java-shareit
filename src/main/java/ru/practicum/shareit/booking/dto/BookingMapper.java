package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {
    public static Booking toBooking(CreateBookingDto createBookingDto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(createBookingDto.getStart());
        booking.setEnd(createBookingDto.getEnd());

        return booking;
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus()
        );

    }

    public static ResponseBookingDto toResponseBookingDto(Booking booking, BookerDto bookerDto, ItemForBookingDto item) {
        return new ResponseBookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                bookerDto,
                item
        );
    }

    public static ItemForBookingDto toItemForBookingDto(Item item) {
        return new ItemForBookingDto(item.getId(), item.getName());
    }

    public static BookerDto toBookerDto(User user) {
        return new BookerDto(user.getId());
    }

    public static BookerDto toBookerDto(Long userId) {
        return new BookerDto(userId);
    }
}
