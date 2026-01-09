package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.CreateCommentException;
import ru.practicum.shareit.exception.IllegalOwnerException;
import ru.practicum.shareit.item.comments.dto.CreateCommentDto;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplUnitTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemServiceImpl itemService;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository, itemRequestRepository);

        owner = new User();
        owner.setId(1L);
        owner.setName("Owner");
        owner.setEmail("owner@test.com");

        booker = new User();
        booker.setId(2L);
        booker.setName("Booker");
        booker.setEmail("booker@test.com");

        item = new Item();
        item.setId(1L);
        item.setName("Item 1");
        item.setOwnerId(owner.getId());
        item.setAvailable(true);
    }


    @Test
    void updateItem_whenUserIsNotOwner_shouldThrowIllegalOwnerException() {
        UpdateItemDto dto = new UpdateItemDto();
        dto.setName("Updated Item");

        when(itemRepository.findById(eq(item.getId()))).thenReturn(Optional.of(item));

        assertThrows(IllegalOwnerException.class, () ->
                itemService.updateItem(dto, item.getId(), booker.getId()));
    }

    @Test
    void getItemById_shouldReturnItem() {
        when(itemRepository.findById(eq(item.getId()))).thenReturn(Optional.of(item));
        when(commentRepository.getAllComment(eq(item.getId()))).thenReturn(List.of());

        ResponseItemDto result = itemService.getItemById(item.getId(), owner.getId());

        assertNotNull(result);
        assertEquals(item.getName(), result.getName());
    }

    @Test
    void createComment_whenBookingNotFound_shouldThrowCreateCommentException() {
        CreateCommentDto dto = new CreateCommentDto();
        dto.setText("Great item!");

        when(itemRepository.findById(eq(item.getId()))).thenReturn(Optional.of(item));
        when(userRepository.findById(eq(booker.getId()))).thenReturn(Optional.of(booker));
        when(bookingRepository.findCanceledByItemAndBooker(eq(item.getId()), eq(booker.getId()), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertThrows(CreateCommentException.class, () ->
                itemService.createComment(dto, item.getId(), booker.getId()));
    }

    @Test
    void searchItem_whenTextIsEmpty_shouldReturnEmptyList() {
        List<ItemDto> result = itemService.searchItem(owner.getId(), "");

        assertTrue(result.isEmpty());
    }
}