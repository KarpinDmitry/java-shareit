package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.IllegalOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PersistenceContext
    private EntityManager em;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@test.com");
        userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@test.com");
        userRepository.save(booker);

        item = new Item();
        item.setName("Item 1");
        item.setDescription("Description 1");
        item.setAvailable(true);
        item.setOwnerId(owner.getId());
        itemRepository.save(item);

        em.flush();
    }

    @Test
    void createBooking_shouldCreateBookingWithWaitingStatus() {
        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(2));

        ResponseBookingDto result = bookingService.createBooking(dto, booker.getId());

        assertNotNull(result.getId());
        assertEquals(BookingStatus.WAITING, result.getStatus());
        assertEquals(item.getId(), result.getItem().getId());
        assertEquals(booker.getId(), result.getBooker().getId());
    }

    @Test
    void createBooking_whenItemNotAvailable_shouldThrowValidationException() {
        item.setAvailable(false);
        itemRepository.save(item);

        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(2));

        assertThrows(ValidationException.class, () ->
                bookingService.createBooking(dto, booker.getId()));
    }

    @Test
    void bookingConfirmation_shouldChangeStatusToApproved() {
        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(2));

        ResponseBookingDto createdBooking = bookingService.createBooking(dto, booker.getId());

        ResponseBookingDto approvedBooking = bookingService.bookingConfirmation(createdBooking.getId(), true, owner.getId());

        assertEquals(BookingStatus.APPROVED, approvedBooking.getStatus());
    }

    @Test
    void getBooking_shouldReturnBookingForBookerOrOwner() {
        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(2));

        ResponseBookingDto createdBooking = bookingService.createBooking(dto, booker.getId());

        ResponseBookingDto result = bookingService.getBooking(createdBooking.getId(), booker.getId());

        assertEquals(createdBooking.getId(), result.getId());
    }

    @Test
    void getBooking_whenUserIsNotOwnerOrBooker_shouldThrowIllegalOwnerException() {
        User otherUser = new User();
        otherUser.setName("Other");
        otherUser.setEmail("other@test.com");
        userRepository.save(otherUser);

        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(2));

        ResponseBookingDto createdBooking = bookingService.createBooking(dto, booker.getId());

        assertThrows(IllegalOwnerException.class, () ->
                bookingService.getBooking(createdBooking.getId(), otherUser.getId()));
    }

    @Test
    void getBookingsByUserAndState_shouldReturnBookingsForBooker() {
        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(2));

        bookingService.createBooking(dto, booker.getId());

        List<ResponseBookingDto> bookings = bookingService.getBookingsByUserAndState(booker.getId(), BookingState.ALL);

        assertFalse(bookings.isEmpty());
        assertEquals(1, bookings.size());
        assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    void getOwnerBookings_shouldReturnBookingsForOwner() {
        CreateBookingDto dto = new CreateBookingDto();
        dto.setItemId(item.getId());
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(2));

        ResponseBookingDto createdBooking = bookingService.createBooking(dto, booker.getId());
        bookingService.bookingConfirmation(createdBooking.getId(), true, owner.getId());

        List<ResponseBookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), BookingState.ALL);

        assertFalse(bookings.isEmpty());
        assertEquals(1, bookings.size());
        assertEquals(item.getId(), bookings.get(0).getItem().getId());
    }
}