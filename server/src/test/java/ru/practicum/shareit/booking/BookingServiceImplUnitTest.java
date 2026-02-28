package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.IllegalOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplUnitTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    private BookingServiceImpl bookingService;

    private User booker;
    private User owner;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);

        booker = new User();
        booker.setId(1L);
        booker.setName("Booker");
        booker.setEmail("booker@test.com");

        owner = new User();
        owner.setId(2L);
        owner.setName("Owner");
        owner.setEmail("owner@test.com");

        item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setOwnerId(owner.getId());
        item.setAvailable(true);

        booking = new Booking();
        booking.setId(1L);
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(2));
    }

    @Test
    void createBooking_shouldSaveBookingWithWaitingStatus() {
        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(2));

        when(userRepository.findById(eq(booker.getId()))).thenReturn(Optional.of(booker));
        when(itemRepository.findById(eq(item.getId()))).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        ResponseBookingDto result = bookingService.createBooking(dto, booker.getId());

        assertNotNull(result);
        assertEquals(BookingStatus.WAITING, result.getStatus());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_whenItemNotAvailable_shouldThrowValidationException() {
        item.setAvailable(false);
        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(2));

        when(userRepository.findById(eq(booker.getId()))).thenReturn(Optional.of(booker));
        when(itemRepository.findById(eq(item.getId()))).thenReturn(Optional.of(item));

        assertThrows(jakarta.validation.ValidationException.class, () ->
                bookingService.createBooking(dto, booker.getId()));
    }

    @Test
    void createBooking_whenBookerIsOwner_shouldThrowValidationException() {
        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(2));

        when(userRepository.findById(eq(owner.getId()))).thenReturn(Optional.of(owner));
        when(itemRepository.findById(eq(item.getId()))).thenReturn(Optional.of(item));

        assertThrows(jakarta.validation.ValidationException.class, () ->
                bookingService.createBooking(dto, owner.getId()));
    }

    @Test
    void bookingConfirmation_shouldApproveBooking() {
        when(bookingRepository.findById(eq(booking.getId()))).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        ResponseBookingDto result = bookingService.bookingConfirmation(booking.getId(), true, owner.getId());

        assertEquals(BookingStatus.APPROVED, result.getStatus());
        verify(bookingRepository).save(argThat(b -> b.getStatus() == BookingStatus.APPROVED));
    }

    @Test
    void bookingConfirmation_whenBookingAlreadyProcessed_shouldThrowValidationException() {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(eq(booking.getId()))).thenReturn(Optional.of(booking));

        assertThrows(jakarta.validation.ValidationException.class, () ->
                bookingService.bookingConfirmation(booking.getId(), true, owner.getId()));
    }

    @Test
    void bookingConfirmation_whenUserIsNotOwner_shouldThrowValidationException() {
        User notOwner = new User();
        notOwner.setId(999L);

        when(bookingRepository.findById(eq(booking.getId()))).thenReturn(Optional.of(booking));

        assertThrows(jakarta.validation.ValidationException.class, () ->
                bookingService.bookingConfirmation(booking.getId(), true, notOwner.getId()));
    }

    @Test
    void getBooking_shouldReturnBookingIfUserIsBooker() {
        when(bookingRepository.findById(eq(booking.getId()))).thenReturn(Optional.of(booking));

        ResponseBookingDto result = bookingService.getBooking(booking.getId(), booker.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getBooking_whenUserIsNotOwnerOrBooker_shouldThrowIllegalOwnerException() {
        User stranger = new User();
        stranger.setId(999L);
        when(bookingRepository.findById(eq(booking.getId()))).thenReturn(Optional.of(booking));

        assertThrows(IllegalOwnerException.class, () ->
                bookingService.getBooking(booking.getId(), stranger.getId()));
    }

    @Test
    void getBookingsByUserAndState_shouldReturnAllBookingsForUser() {
        when(userRepository.findById(eq(booker.getId()))).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdOrderByStartDesc(eq(booker.getId()))).thenReturn(List.of(booking));

        List<ResponseBookingDto> result = bookingService.getBookingsByUserAndState(booker.getId(), BookingState.ALL);

        assertEquals(1, result.size());
        verify(bookingRepository).findByBookerIdOrderByStartDesc(eq(booker.getId()));
    }

    @Test
    void getOwnerBookings_shouldReturnBookingsForOwnerItems() {
        when(userRepository.findById(eq(owner.getId()))).thenReturn(Optional.of(owner));
        when(itemRepository.getUserItems(eq(owner.getId()))).thenReturn(List.of(item));
        when(bookingRepository.findByItemIdsOrderByStartDesc(any())).thenReturn(List.of(booking));

        List<ResponseBookingDto> result = bookingService.getOwnerBookings(owner.getId(), BookingState.ALL);

        assertEquals(1, result.size());
        verify(bookingRepository).findByItemIdsOrderByStartDesc(any());
    }
}