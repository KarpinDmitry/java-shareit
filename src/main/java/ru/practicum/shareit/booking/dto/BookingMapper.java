package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {
    public static Booking toBooking(CreateBookingDto createBookingDto, Long booker){
        Booking booking = new Booking();
        booking.setItemId(createBookingDto.getItemId());
        booking.setBookerId(booker);
        booking.setStart(createBookingDto.getStart());
        booking.setEnd(createBookingDto.getEnd());

        return booking;
    }

    public static BookingDto toBookingDto(Booking booking){
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                booking.getBookerId(),
                booking.getStatus()
        );

    }

    public static ResponseBookingDto toResponseBookingDto(Booking booking, BookerDto bookerDto, ItemForBookingDto item){
        return new ResponseBookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                bookerDto,
                item
        );
    }

    public static ItemForBookingDto toItemForBookingDto(Item item){
        return new ItemForBookingDto(item.getId(), item.getName());
    }

    public static BookerDto toBookerDto(User user){
        return new BookerDto(user.getId());
    }

    public static BookerDto toBookerDto(Long userId){
        return new BookerDto(userId);
    }
}
