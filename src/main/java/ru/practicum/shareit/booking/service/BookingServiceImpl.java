package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IllegalOwnerException;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseBookingDto createBooking(CreateBookingDto createBookingDto, Long bookerId) {

        //Проверка наличия booker и item
        User user = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundIdException("Id: " + bookerId + " not found"));
        Item item = itemRepository.findById(createBookingDto.getItemId())
                .orElseThrow(() -> new NotFoundIdException("Item с id: "
                        + createBookingDto.getItemId() + " не найден"));

        if (item.getOwnerId().equals(bookerId)) {
            throw new ValidationException("Нельзя забронировать собственную вещь");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }

        LocalDateTime start = createBookingDto.getStart();
        LocalDateTime end = createBookingDto.getEnd();

        if (!start.isBefore(end)) {
            throw new ValidationException("Дата окончания должна быть позже даты начала");
        }

        Booking booking = BookingMapper.toBooking(createBookingDto, user, item);

        booking.setStatus(BookingStatus.WAITING);

        ItemForBookingDto itemForBookingDto = BookingMapper.toItemForBookingDto(item);
        BookerDto bookerDto = BookingMapper.toBookerDto(user);

        return BookingMapper.toResponseBookingDto(bookingRepository.save(booking), bookerDto, itemForBookingDto);
    }

    @Override
    public ResponseBookingDto bookingConfirmation(Long bookingId, Boolean approved, Long ownerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundIdException("Id bookingId: " + bookingId + " not found"));

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException("Нельзя подтвердить бронирование, которое уже обработано");
        }

        Item item = booking.getItem();

        if (!item.getOwnerId().equals(ownerId)) {
            throw new ValidationException("Подтвердить бронь может только владелец");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        bookingRepository.save(booking);

        ItemForBookingDto itemForBookingDto = BookingMapper.toItemForBookingDto(item);

        BookerDto bookerDto = BookingMapper.toBookerDto(booking.getBooker());

        return BookingMapper.toResponseBookingDto(booking, bookerDto, itemForBookingDto);
    }

    @Override
    public ResponseBookingDto getBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundIdException("Id bookingId: " + bookingId + " not found"));

        Item item = booking.getItem();

        if (!booking.getBooker().getId().equals(userId) && !item.getOwnerId().equals(userId)) {
            throw new IllegalOwnerException("Получить данные о бронировании может "
                    + "только владелец вещи или бронирования");
        }

        ItemForBookingDto itemForBookingDto = BookingMapper.toItemForBookingDto(item);

        BookerDto bookerDto = BookingMapper.toBookerDto(booking.getBooker());

        return BookingMapper.toResponseBookingDto(booking, bookerDto, itemForBookingDto);
    }

    @Override
    public List<ResponseBookingDto> getBookingsByUserAndState(Long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundIdException("Пользователь с Id: " + userId + " не найден"));

        List<Booking> bookingList;
        if (state.equals(BookingState.ALL)) {
            bookingList = bookingRepository.findByBookerIdOrderByStartDesc(userId);
        } else if (state.equals(BookingState.REJECTED)) {
            bookingList = bookingRepository.findBookingsByUserAndStatus(userId, BookingStatus.REJECTED);
        } else if (state.equals(BookingState.CURRENT)) {
            bookingList = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now());
        } else if (state.equals(BookingState.PAST)) {
            bookingList = bookingRepository.findByBookerIdAndEndBefore(userId, LocalDateTime.now());
        } else if (state.equals(BookingState.FUTURE)) {
            bookingList = bookingRepository.findByBookerIdAndStartAfter(userId, LocalDateTime.now());
        } else {
            bookingList = bookingRepository.findBookingsByUserAndStatus(userId, BookingStatus.WAITING);
        }

        List<ResponseBookingDto> result = new ArrayList<>();

        for (Booking booking : bookingList) {
            Item item = booking.getItem();

            ItemForBookingDto itemForBookingDto = BookingMapper.toItemForBookingDto(item);

            BookerDto bookerDto = BookingMapper.toBookerDto(booking.getBooker());

            result.add(BookingMapper.toResponseBookingDto(booking, bookerDto, itemForBookingDto));
        }

        return result;
    }

    @Override
    public List<ResponseBookingDto> getOwnerBookings(Long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundIdException("Пользователь с Id: " + userId + " не найден"));

        List<Item> itemList = itemRepository.getUserItems(userId);

        List<Long> itemIdList = itemList.stream().map(Item::getId).toList();

        if (itemList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Booking> bookingList;
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                bookingList = bookingRepository.findByItemIdsOrderByStartDesc(itemIdList);
                break;
            case CURRENT:
                bookingList = bookingRepository.findByItemIdsAndCurrent(itemIdList, now);
                break;
            case PAST:
                bookingList = bookingRepository.findByItemIdsAndPast(itemIdList, now);
                break;
            case FUTURE:
                bookingList = bookingRepository.findByItemIdsAndFuture(itemIdList, now);
                break;
            case WAITING:
                bookingList = bookingRepository.findByItemIdsAndStatus(itemIdList, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookingList = bookingRepository.findByItemIdsAndStatus(itemIdList, BookingStatus.REJECTED);
                break;
            default:
                throw new IllegalArgumentException("Неизвестный статус: " + state);
        }

        List<ResponseBookingDto> result = new ArrayList<>();

        for (Booking booking : bookingList) {
            Item item = booking.getItem();

            ItemForBookingDto itemForBookingDto = BookingMapper.toItemForBookingDto(item);

            BookerDto bookerDto = BookingMapper.toBookerDto(booking.getBooker());

            result.add(BookingMapper.toResponseBookingDto(booking, bookerDto, itemForBookingDto));
        }

        return result;
    }
}
