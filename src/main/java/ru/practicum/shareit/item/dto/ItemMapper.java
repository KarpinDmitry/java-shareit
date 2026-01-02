package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comments.dto.ResponseCommentDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId() != null ? item.getRequestId() : null
        );
    }

    public static Item toItem(CreateItemDto createItemDto, Long owner) {
        return new Item(
                null,
                createItemDto.getName(),
                createItemDto.getDescription(),
                createItemDto.getAvailable(),
                owner,
                null
        );
    }


    public static Item toItem(UpdateItemDto updateItemDto, Item item) {
        if (updateItemDto.getName() != null) {
            item.setName(updateItemDto.getName());
        }
        if (updateItemDto.getDescription() != null) {
            item.setDescription(updateItemDto.getDescription());
        }
        if (updateItemDto.getAvailable() != null) {
            item.setAvailable(updateItemDto.getAvailable());
        }
        return item;
    }

    public static ResponseItemDto toResponseItemDto(Item item, BookingForItemDto last,
                                                    BookingForItemDto next, List<ResponseCommentDto> comments) {
        return new ResponseItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                last,
                next,
                comments);
    }

    public static BookingForItemDto toBookingForItemDto(Booking booking) {
        return new BookingForItemDto(booking.getId(), booking.getBooker().getId());
    }

}
