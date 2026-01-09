package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.CreateCommentException;
import ru.practicum.shareit.exception.IllegalOwnerException;
import ru.practicum.shareit.exception.NotFoundIdException;
import ru.practicum.shareit.item.comments.dto.CommentMapper;
import ru.practicum.shareit.item.comments.dto.CreateCommentDto;
import ru.practicum.shareit.item.comments.dto.ResponseCommentDto;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto createItem(CreateItemDto createItemDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundIdException("User c id " + userId + " не найден"));

        Item item;
        if (createItemDto.getRequestId() != null) {
            ItemRequest request = itemRequestRepository.findById(createItemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundIdException("Запрос c id: " + createItemDto.getRequestId() + " не найден"));

            item = ItemMapper.toItem(createItemDto, userId, request);
        } else {
            item = ItemMapper.toItem(createItemDto, userId, null);
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(UpdateItemDto updateItemDto, Long id, Long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Item с id: " + id + "не найден"));

        Item updateItem = ItemMapper.toItem(updateItemDto, item);
        if (!item.getOwnerId().equals(userId)) {
            throw new IllegalOwnerException("Владелец не совпадает c пользователем: "
                    + item.getOwnerId() + "!=" + userId);
        }
        return ItemMapper.toItemDto(itemRepository.save(updateItem));

    }

    @Override
    public ResponseItemDto getItemById(Long id, Long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Item с id: " + id + "не найден"));

        List<ResponseCommentDto> responseCommentDto = CommentMapper
                .toResponseCommentDto(commentRepository.getAllComment(item.getId()));

        if (!item.getOwnerId().equals(userId)) {
            return ItemMapper.toResponseItemDto(item, null, null, responseCommentDto);
        }

        Booking bookingNext = bookingRepository.findByItemIdNextBooking(id, LocalDateTime.now()).orElse(null);
        Booking bookingLast = bookingRepository.findByItemIdLastBooking(id, LocalDateTime.now()).orElse(null);

        BookingForItemDto lastDto =
                bookingLast == null ? null : ItemMapper.toBookingForItemDto(bookingLast);

        BookingForItemDto nextDto =
                bookingNext == null ? null : ItemMapper.toBookingForItemDto(bookingNext);

        return ItemMapper.toResponseItemDto(item, lastDto, nextDto, responseCommentDto);
    }

    @Override
    public List<ResponseItemDto> getUserItems(Long userId) {
        List<Item> itemList = itemRepository.getUserItems(userId);

        List<ResponseItemDto> result = new ArrayList<>();

        for (Item item : itemList) {
            Booking bookingNext = bookingRepository.findByItemIdNextBooking(item.getId(),
                    LocalDateTime.now()).orElse(null);

            Booking bookingLast = bookingRepository.findByItemIdLastBooking(item.getId(),
                    LocalDateTime.now()).orElse(null);

            BookingForItemDto lastDto =
                    bookingLast == null ? null : ItemMapper.toBookingForItemDto(bookingLast);

            BookingForItemDto nextDto =
                    bookingNext == null ? null : ItemMapper.toBookingForItemDto(bookingNext);

            List<ResponseCommentDto> responseCommentDto = CommentMapper
                    .toResponseCommentDto(commentRepository.getAllComment(item.getId()));

            result.add(ItemMapper.toResponseItemDto(item, lastDto, nextDto, responseCommentDto));
        }
        return result;
    }

    @Override
    public List<ItemDto> searchItem(Long userId, String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return itemRepository.search(text).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public ResponseCommentDto createComment(CreateCommentDto createCommentDto, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CreateCommentException("Item с id: " + itemId + " не найден"));

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new CreateCommentException("User c id " + userId + " не найден"));
        Optional<Booking> booking = bookingRepository.findById(itemId);

        LocalDateTime now = LocalDateTime.now().plusHours(4); // Расхождение с UTC

        Optional<Booking> bookingOptional = bookingRepository
                .findCanceledByItemAndBooker(itemId, userId, now);

        if (bookingOptional.isEmpty()) {
            throw new CreateCommentException("Бронирование не найдено");
        }

        Comment comment = CommentMapper.toComment(createCommentDto, item, author, LocalDate.now());
        commentRepository.save(comment);

        return CommentMapper.toResponseCommentDto(comment);
    }
}
