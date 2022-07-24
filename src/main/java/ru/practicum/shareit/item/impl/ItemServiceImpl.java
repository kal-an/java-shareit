package ru.practicum.shareit.item.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository repository,
                           CommentRepository commentRepository,
                           UserService userService,
                           BookingRepository bookingRepository) {
        this.repository = repository;
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        final Item item = ItemMapper.toItem(itemDto);
        userService.getUserById(userId);
        item.setOwner(userId);
        if (itemDto.getId() != null) {
            log.error("Item ID should be empty {}", item);
            throw new InvalidEntityException("Item ID should be empty");
        }
        final Item saveItemInDb = repository.save(item);
        log.info("Item {} saved", saveItemInDb);
        return ItemMapper.toItemDto(saveItemInDb);
    }

    @Override
    public ItemDto editItem(ItemDto itemDto, long itemId, long userId) {
        final Item itemInDb = repository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", itemId)));
        userService.getUserById(userId);
        if (!itemInDb.getOwner().equals(userId)) {
            log.error("UserID not equal item owner");
            throw new UserNotFoundException("UserID should be item owner");
        }
        if (itemDto.getAvailable() != null) itemInDb.setAvailable(itemDto.getAvailable());
        if (itemDto.getName() != null) itemInDb.setName(itemDto.getName());
        if (itemDto.getDescription() != null) itemInDb.setDescription(itemDto.getDescription());
        final Item updateItemInDb = repository.save(itemInDb);
        log.info("Item {} updated", updateItemInDb);
        return ItemMapper.toItemDto(updateItemInDb);
    }

    @Override
    public ItemDtoExtended getItemById(long id, long userId) {
        userService.getUserById(userId);
        final Item item = repository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", id)));
        final ItemDtoExtended dtoExtended = new ItemDtoExtended(ItemMapper.toItemDto(item));
        LocalDateTime now = LocalDateTime.now();
        if (item.getOwner().equals(userId)) {
            List<Booking> lastBookingList = bookingRepository
                    .findByItemIdAndEndBeforeOrderByEndDesc(id, now);
            List<Booking> nextBookingList = bookingRepository
                    .findByItemIdAndStartAfterOrderByEndDesc(id, now);
            setBookingDates(lastBookingList, nextBookingList, dtoExtended);
        }
        List<Comment> comments = commentRepository.findAllByItemId(id);
        for (Comment comment : comments) {
            final UserDto user = userService.getUserById(comment.getAuthorId());
            comment.setAuthorName(user.getName());
        }
        dtoExtended.setComments(comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));
        return dtoExtended;
    }

    @Override
    public List<ItemDtoExtended> getAllOwnerItems(long ownerId) {
        userService.getUserById(ownerId);
        final List<ItemDtoExtended> itemDtoExtendedList = new ArrayList<>();
        final List<Item> items = repository.findAllItemsByOwner(ownerId);
        LocalDateTime now = LocalDateTime.now();
        for (Item item : items) {
            List<Booking> lastBookingList = bookingRepository
                    .findByItemIdAndEndBeforeOrderByEndDesc(item.getId(), now);
            List<Booking> nextBookingList = bookingRepository
                    .findByItemIdAndStartAfterOrderByEndDesc(item.getId(), now);
            final ItemDtoExtended dtoExtended = new ItemDtoExtended(ItemMapper.toItemDto(item));
            setBookingDates(lastBookingList, nextBookingList, dtoExtended);
            List<Comment> comments = commentRepository.findAllByItemId(item.getId());
            for (Comment comment : comments) {
                final UserDto user = userService.getUserById(comment.getAuthorId());
                comment.setAuthorName(user.getName());
            }
            dtoExtended.setComments(comments.stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList()));
            itemDtoExtendedList.add(dtoExtended);
        }
        return itemDtoExtendedList;
    }

    private void setBookingDates(List<Booking> lastBookingList, List<Booking> nextBookingList,
                                 ItemDtoExtended dtoExtended) {
        if (!lastBookingList.isEmpty()) {
            dtoExtended.setLastBooking(new DateBooking(
                    lastBookingList.get(0).getId(),
                    lastBookingList.get(0).getBookerId(),
                    lastBookingList.get(0).getStart(),
                    lastBookingList.get(0).getEnd()
            ));
        }
        if (!nextBookingList.isEmpty()) {
            dtoExtended.setNextBooking(new DateBooking(
                    nextBookingList.get(0).getId(),
                    nextBookingList.get(0).getBookerId(),
                    nextBookingList.get(0).getStart(),
                    nextBookingList.get(0).getEnd()
            ));
        }
    }

    @Override
    public List<ItemDto> searchItemsForBooking(String text) {
        if (text.isEmpty()) {
            log.info("Text is empty");
            return Collections.emptyList();
        }
        return repository.searchItemsForBooking(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long userId, long itemId,
                                 CommentCreationDto textDto) {
        log.info("input text {} ", textDto);
        final User user = UserMapper.toUser(userService.getUserById(userId));
        final Item item = repository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", itemId)));
        List<Booking> bookings = bookingRepository
                .findByItemIdAndBookerIdAndStatusAndEndIsBefore(itemId, userId,
                Status.APPROVED, LocalDateTime.now());
        if (bookings.isEmpty()) {
            log.error("User {} can't leave comment to item {}", userId, itemId);
            throw new InvalidEntityException("User can't leave comment");
        }
        final Comment savedCommentInDb = commentRepository.save(
                new Comment(textDto.getText(), itemId, userId, user.getName()));
        log.info("Comment {} saved", savedCommentInDb);
        return CommentMapper.toCommentDto(savedCommentInDb);
    }

    @Override
    public List<CommentDto> searchCommentsForItem(long itemId, String text) {
        if (text.isEmpty()) {
            log.info("Text is empty");
            return Collections.emptyList();
        }
        return commentRepository.findCommentsByItemIdAndTextContains(itemId, text).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
